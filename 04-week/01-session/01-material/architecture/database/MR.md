# Flight Management System – Refined Data Model

## 🧭 Conventions
- Tables and columns use **snake_case**, in **singular**.  
- Primary keys are **UUID** generated with `gen_random_uuid()` (`CREATE EXTENSION pgcrypto;`).  
- **Audit fields (7):**  
  - `status`  
  - `created_at` / `created_by`  
  - `updated_at` / `updated_by`  
  - `deleted_at` / `deleted_by`  
- Foreign Keys use **ON UPDATE CASCADE** and **ON DELETE** rules appropriate to the domain.  
- **Unique constraints** on codes and names (scoped where applicable).  

---

## Module: Geolocation
- **continent** {id, code, name, description}  
- **country** {id, code, name, description, continent_id}  
- **state** {id, code, name, description, country_id}  
- **city** {id, code, name, description, state_id}  

## Module: Infrastructure
- **airport** {id, code, name, address, city_id}  
- **terminal** {id, code, name, airport_id}  
- **boarding_gate** {id, code, name, terminal_id}  

## Module: Parameterization
- **document_type** {id, code, name, description}  
- **flight_type** {id, code, name, description}  
- **aircraft_type** {id, code, name, description}  
- **crew_role** {id, code, name, description}  
- **ticket_class** {id, code, name, description}  

## Module: Security
- **role** {id, name, description}  
- **permission** {id, code, name, description, resource}  
- **role_permission** {id, role_id, permission_id, can_create, can_read, can_update, can_delete}  
- **module** {id, name, color, icon, path}  
- **form** {id, name, color, icon, path, section}  
- **form_module** {id, form_id, module_id}  
- **role_form** {id, role_id, form_id}  
- **person** {id, first_name, last_name, document_number, birth_date, gender, phone, email, address, attendant_name, attending_phone, document_type_id, city_id}  
- **user_account** {id, username, password_hash, person_id, role_id, is_active, last_login, email_verified, two_factor_enabled}  
- **session** {id, user_account_id, token, ip_address, user_agent, login_at, logout_at, expires_at}  
- **audit_log** {id, user_account_id, action, table_name, record_id, old_values, new_values, ip_address}  
- **password_reset** {id, user_account_id, token, requested_at, expires_at, used_at}  
- **user_preference** {id, user_account_id, theme, language, timezone, notifications_enabled}  
- **security_question** {id, code, question_text}  
- **user_security_answer** {id, user_account_id, security_question_id, answer_hash}  

## Module: Human Resources
- **employee** {id, employee_number, salary, hire_date, termination_date, crew_role_id, person_id}  
- **contract** {id, contract_number, contract_type, start_date, end_date, salary_amount, terms, employee_id}  
- **certification** {id, code, name, description, issuing_authority, validity_period_months}  
- **employee_certification** {id, employee_id, certification_id, issue_date, expiry_date, certificate_number}  
- **training_program** {id, code, name, description, duration_hours, max_participants}  
- **employee_training** {id, employee_id, training_program_id, enrollment_date, completion_date, score, instructor_name}  
- **payroll** {id, employee_id, period_start, period_end, base_salary, bonuses, deductions, net_salary, payment_date}  
- **attendance** {id, employee_id, date, check_in_time, check_out_time, hours_worked, attendance_type}  
- **leave_request** {id, employee_id, leave_type, start_date, end_date, reason, status, approved_by, approved_at}  
- **performance_review** {id, employee_id, review_date, reviewer_id, rating, comments, next_review_date}  

## Module: Aircraft Management
- **aircraft** {id, manufacturer, model, registration_code, serial_number, manufacturing_date, acquisition_date, hours_in_use, cycles_completed, capacity, aircraft_type_id}  
- **aircraft_status** {id, code, name, description}  
- **aircraft_status_history** {id, aircraft_id, aircraft_status_id, change_date, reason, changed_by}  
- **maintenance_type** {id, code, name, description, frequency_hours, frequency_cycles}  
- **maintenance_schedule** {id, aircraft_id, maintenance_type_id, scheduled_date, due_hours, due_cycles, status}  
- **maintenance_record** {id, aircraft_id, maintenance_type_id, maintenance_date, technician_id, hours_at_maintenance, cycles_at_maintenance, findings, actions_taken, next_due_hours, next_due_cycles}  
- **aircraft_component** {id, code, name, description, part_number, manufacturer}  
- **component_installation** {id, aircraft_id, aircraft_component_id, installation_date, removal_date, hours_at_installation, cycles_at_installation, serial_number, location}  
- **fuel_log** {id, aircraft_id, refuel_date, quantity_liters, fuel_type, cost, airport_id, flight_id}  
- **incident_report** {id, aircraft_id, flight_id, incident_date, incident_type, severity, description, reported_by, resolution, resolved_at}  

## Module: Flight Operations
- **flight** {id, flight_date, departure_time, arrival_time, flight_type_id, origin_boarding_gate_id, destination_boarding_gate_id, aircraft_id}  
- **crew_assignment** {id, flight_id, employee_id, crew_role_id}  
- **ticket** {id, code, flight_id, passenger_id, ticket_class_id, seat_number, price}  
- **baggage** {id, code, weight, ticket_id}  

## Module: Passengers & Services
- **passenger** {id, frequent_flyer_number, person_id}  

## Module: Notifications
- **message_template** {id, code, name, subject_template, body_template}  
- **notification** {id, person_id, channel, subject, message, sent_at, status}  
