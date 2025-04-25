INSERT INTO MENU (name, description, url, icon, roles, is_active,parent_id)
    values
    ('parentMenu01', '부모메뉴1','/parentMenu01','mdi-home','SYSTEM_ADMIN',true, null),
    ('parentMenu02', '부모메뉴2','/parentMenu02','mdi-home','SYSTEM_ADMIN',true, null),
    ('parentMenu03', '부모메뉴3','/parentMenu03','mdi-home','SYSTEM_ADMIN',true, null),
    ('parentMenu04', '부모메뉴4','/parentMenu04','mdi-home','SYSTEM_ADMIN',true, null),
    ('parentMenu05', '부모메뉴5','/parentMenu05','mdi-home','SYSTEM_ADMIN',false, null),
    ('parentMenu01_child01', '부모메뉴1_자식메뉴1','/parentMenu01_child01','mdi-account-circle','SYSTEM_ADMIN',true,1),
    ('parentMenu01_child02', '부모메뉴1_자식메뉴2','/parentMenu01_child02','mdi-account-circle','SYSTEM_ADMIN',true,1),
    ('parentMenu01_child03', '부모메뉴1_자식메뉴3','/parentMenu01_child03','mdi-account-circle','SYSTEM_ADMIN',true,1),
    ('parentMenu01_child04', '부모메뉴1_자식메뉴4','/parentMenu01_child04','mdi-account-circle','SYSTEM_ADMIN',false,1);

