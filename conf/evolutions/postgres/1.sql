# --- !Ups

create table STATES (id INTEGER NOT NULL PRIMARY KEY, 
                     name VARCHAR(15) NOT NULL);
create table TASKS (id SERIAL NOT NULL PRIMARY KEY, 
                    title VARCHAR(30) NOT NULL, 
                    description VARCHAR(500) NOT NULL, 
                    state_id INTEGER NOT NULL);

insert into states(id, name) values(1, 'TODO');                      
insert into states(id, name) values(2, 'In Progress');
insert into states(id, name) values(3, 'Done');
       

# --- !Downs

drop table tasks;
drop table states;
