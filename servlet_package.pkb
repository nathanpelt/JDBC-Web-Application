create or replace package body servlet_package as


  procedure myinsert
    (
      i_first_name    in employees.first_name%type,
      i_last_name     in employees.last_name%type,
      i_email         in employees.email%type,
      i_phone_number  in employees.phone_number%type,
      i_hire_date     in varchar2,
      i_salary        in varchar2
    )
    
  as
    v_sqlerrm  varchar2(200);   
    
  begin
    insert into employees (employee_id, first_name, last_name, email, phone_number, hire_date, job_id, salary)
    values (employees_seq.nextval, i_first_name, i_last_name, i_email, i_phone_number, to_date(i_hire_date,'DD-MON-YY'), 'ST_MAN', to_number(i_salary));
    commit;
   -- 'YYYY-MM-DD HH:MI:SS AM' 
  exception
    when others then
    v_sqlerrm := SQLERRM;
    rollback;
    dbms_output.put_line('myinsert procedure failed (rollback occured)-' || v_sqlerrm);
  end myinsert;
  
  
  
  procedure mydelete
  (
    i_employee_id    in employees.employee_id%type
  )
  as
    v_sqlerrm  varchar2(200);   
    
  begin
    delete from employees where employee_id = i_employee_id;
    commit;
  exception
    when others then
    v_sqlerrm := SQLERRM;
    rollback;
    dbms_output.put_line('mydelete procedure failed (rollback occured)-' || v_sqlerrm);
  end mydelete;
  
  
  
  procedure myupdate
  (
    i_employee_id   in employees.employee_id%type,
    i_first_name    in employees.first_name%type,
    i_last_name     in employees.last_name%type,
    i_email         in employees.email%type,
    i_phone_number  in employees.phone_number%type,
    i_hire_date     in varchar2,
    i_salary        in out varchar2
  )
  as
    v_sqlerrm  varchar2(200);  
    v_sqlerrcode varchar2(200);
    
  begin
    update employees set first_name = i_first_name, last_name = i_last_name, email = i_email, 
    phone_number = i_phone_number, hire_date = to_date(i_hire_date,'YYYY-MM-DD HH24:MI:SS'), salary = i_salary
    where employee_id = i_employee_id;
    commit;
  exception
    when others then
    v_sqlerrm := SQLERRM;
    --v_sqlerrcode := SQLERRCODE;
    rollback;
    dbms_output.put_line('myupdate procedure failed (rollback occured)-' || v_sqlerrm);
    --raise_application_error(v_sqlerrcode,v_sqlerrm);
  end myupdate;
  
  
  
  procedure myselect
    (
      o_selectout     out SYS_REFCURSOR
    )
  as
    v_sqlerrm  varchar2(200);
  begin
      open o_selectout for 
      select * from employees;
    
  exception
    when others then
    v_sqlerrm := SQLERRM;
    dbms_output.put_line('myselect procedure failed (rollback occured)-' || v_sqlerrm);
  end myselect;
  
  
  
  procedure selectrecord
    (
      o_selectout     out SYS_REFCURSOR,
      i_employee_id   in employees.employee_id%type
    )
  as
    v_sqlerrm  varchar2(200);
  begin
      open o_selectout for 
      select * from employees where employee_id = i_employee_id;
  
  exception
    when others then
    v_sqlerrm := SQLERRM;
    dbms_output.put_line('selectrecord procedure failed (rollback occured)-' || v_sqlerrm);
  end selectrecord;
  
  
  
  function max_employees
      return boolean 
    is
    
      v_employee_count  number;
      v_return_value    boolean;
      v_max_number      constant number := 111;
    begin
      select count(*) into v_employee_count from employees;
      
      if(v_max_number > v_employee_count)
      then
        v_return_value := true;
      else
        v_return_value := false;
      end if;
      
      return v_return_value;
    end max_employees;
  

end servlet_package;
