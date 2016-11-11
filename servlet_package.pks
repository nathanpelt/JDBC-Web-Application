create or replace package servlet_package as 

  procedure myinsert
    (
      i_first_name    in employees.first_name%type,
      i_last_name     in employees.last_name%type,
      i_email         in employees.email%type,
      i_phone_number  in employees.phone_number%type,
      i_hire_date     in varchar2,
      i_salary        in varchar2
    );
    
  procedure mydelete
  (
    i_employee_id    in employees.employee_id%type
  );
  
  procedure myupdate
  (
    i_employee_id    in employees.employee_id%type,
    i_first_name    in employees.first_name%type,
    i_last_name     in employees.last_name%type,
    i_email         in employees.email%type,
    i_phone_number  in employees.phone_number%type,
    i_hire_date     in varchar2,
    i_salary        in out varchar2
  );
    
  procedure myselect
    (
      o_selectout     out SYS_REFCURSOR
    );
    
  procedure selectrecord
    (
      o_selectout     out SYS_REFCURSOR,
      i_employee_id   in employees.employee_id%type
    );
    
  function max_employees
      return boolean;

end servlet_package;
