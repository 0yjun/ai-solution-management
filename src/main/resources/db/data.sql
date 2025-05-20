INSERT INTO member (
    username,
    password,
    role,
    description,
    last_login_at,
    login_fail_count
) VALUES
      ('SYSTEM_ADMIN',   '$2a$10$J5XR/L4Nb6DFw5HopHoXHeED.PMiyoeXLeqzKg8ad3om8YffcuB4a', 'SYSTEM_ADMIN',   NULL, NULL, 0),
      ('SYSTEM_MANAGER', '$2a$10$J5XR/L4Nb6DFw5HopHoXHeED.PMiyoeXLeqzKg8ad3om8YffcuB4a', 'SYSTEM_MANAGER', NULL, NULL, 0),
      ('CONTENT_MANAGER','$2a$10$J5XR/L4Nb6DFw5HopHoXHeED.PMiyoeXLeqzKg8ad3om8YffcuB4a', 'CONTENT_MANAGER',NULL, NULL, 0),
      ('CONTENT_ADMIN',  '$2a$10$J5XR/L4Nb6DFw5HopHoXHeED.PMiyoeXLeqzKg8ad3om8YffcuB4a', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('1234',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'SYSTEM_ADMIN',  NULL, NULL, 0),
      ('test',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test1',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test2',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test3',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test4',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test5',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test6',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test7',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test8',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test9',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test10',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test11',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test12',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test13',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test14',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test15',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test16',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test17',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test18',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0),
      ('test19',  '$2a$10$UDUMvMkKYyRVuX4WZqi8aOGCGwcRK4DP9lPNUAc0pcFlneetL4f8y', 'CONTENT_ADMIN',  NULL, NULL, 0);





INSERT INTO MENU (name, description, url, seq, icon, roles, is_active,parent_id,prev_menu_id, next_menu_id)
    values
        -- 부모메뉴
    ('DashBoard', 'DashBoard','/',1,'mdi-view-dashboard','CONTENT_MANAGER,CONTENT_ADMIN,SYSTEM_MANAGER,SYSTEM_ADMIN',true, null,null,null),
    ('학습관리', '학습관리','',2,'mdi-clipboard','CONTENT_MANAGER,CONTENT_ADMIN,SYSTEM_MANAGER,SYSTEM_ADMIN',true, null,null,null),
    ('모델관리', '모델관리','',3,'mdi-lan-connect','CONTENT_MANAGER,CONTENT_ADMIN,SYSTEM_MANAGER,SYSTEM_ADMIN',true, null,null,null),
    ('시스템 설정', '시스템 설정','',4,'mdi-cog','CONTENT_ADMIN,SYSTEM_ADMIN',true, null,null,null),
    ('통계조회', '통계조회','',5,'mdi-database-clock','CONTENT_ADMIN,SYSTEM_ADMIN',true, null,null,null),
    ('배포관리', '배포관리','',3,'mdi-database-clock','CONTENT_ADMIN,SYSTEM_ADMIN',true, null,null,null),

    -- 학습관리 하위 메뉴
    ('학습문서', '학습문서','/learning-management/document-ingestion',3,'mdi-clipboard-edit','SYSTEM_ADMIN',true,2,null,null),
    ('학습 데이터 생성', '학습 데이터 생성','/learning-management/data-generation',2,'mdi-book-plus','SYSTEM_ADMIN',true,2,null,null),
    ('벡터데이터 관리', '벡터데이터 관리','/learning-management/vector-management',1,'mdi-database-edit-outline','SYSTEM_ADMIN',true,2,null,null),

    -- 시스템 설정
    ('메뉴/권한관리', '메뉴/권한관리','/system/menu-management',1,'mdi-menu-open','SYSTEM_ADMIN',true,4,null,7),
    ('사용자 관리', '사용자 관리','/system/member-management',2,'mdi-account-edit','SYSTEM_ADMIN',true,4,6,8),
    ('공통코드 관리', '공통코드 관리','/system/commoncode-management',3,'mdi-file-code','SYSTEM_ADMIN',true,4,7,null),
    ('임계치 관리', '임계치관리','/system/threshold-management',4,'mdi-speedometer','SYSTEM_ADMIN',true, 4,null,null),
    ('도움말 관리', '도움말 관리','/system/help-dialog',5,'mdi-help-circle','SYSTEM_ADMIN',true, 4,null,null),

    -- 통계 관리
    ('이용률 통계', '메뉴/권한관리','/statistics/utilization-rate',1,'mdi-database-search-outline','SYSTEM_ADMIN',true,5,null,null),
    ('키워드 통계', '사용자 관리','/statistics/keyword-statistics',2,'mdi-database-search-outline','SYSTEM_ADMIN',true,5,null,null),
    ('답변정확도 통계', '공통코드 관리','/statistics/answer-accuracy-statistics',3,'mdi-database-search-outline','SYSTEM_ADMIN',true,5,null,null);

