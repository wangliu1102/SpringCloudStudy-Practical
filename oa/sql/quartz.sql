-- ----------------------------
-- 1、存储每一个已配置的 jobdetail 的详细信息
-- ----------------------------
drop table if exists qrtz_job_details;
create table qrtz_job_details (
    sched_name           varchar(120)    not null,
    job_name             varchar(200)    not null,
    job_group            varchar(200)    not null,
    description          varchar(250)    null,
    job_class_name       varchar(250)    not null,
    is_durable           varchar(1)      not null,
    is_nonconcurrent     varchar(1)      not null,
    is_update_data       varchar(1)      not null,
    requests_recovery    varchar(1)      not null,
    job_data             blob            null,
    primary key (sched_name,job_name,job_group)
) engine=innodb default charset=utf8;

-- ----------------------------
-- 2、 存储已配置的 trigger 的信息
-- ----------------------------
drop table if exists qrtz_triggers;
create table qrtz_triggers (
    sched_name           varchar(120)    not null,
    trigger_name         varchar(200)    not null,
    trigger_group        varchar(200)    not null,
    job_name             varchar(200)    not null,
    job_group            varchar(200)    not null,
    description          varchar(250)    null,
    next_fire_time       bigint(13)      null,
    prev_fire_time       bigint(13)      null,
    priority             integer         null,
    trigger_state        varchar(16)     not null,
    trigger_type         varchar(8)      not null,
    start_time           bigint(13)      not null,
    end_time             bigint(13)      null,
    calendar_name        varchar(200)    null,
    misfire_instr        smallint(2)     null,
    job_data             blob            null,
    primary key (sched_name,trigger_name,trigger_group),
    foreign key (sched_name,job_name,job_group) references qrtz_job_details(sched_name,job_name,job_group)
) engine=innodb default charset=utf8;

-- ----------------------------
-- 3、 存储简单的 trigger，包括重复次数，间隔，以及已触发的次数
-- ----------------------------
drop table if exists qrtz_simple_triggers;
create table qrtz_simple_triggers (
    sched_name           varchar(120)    not null,
    trigger_name         varchar(200)    not null,
    trigger_group        varchar(200)    not null,
    repeat_count         bigint(7)       not null,
    repeat_interval      bigint(12)      not null,
    times_triggered      bigint(10)      not null,
    primary key (sched_name,trigger_name,trigger_group),
    foreign key (sched_name,trigger_name,trigger_group) references qrtz_triggers(sched_name,trigger_name,trigger_group)
) engine=innodb default charset=utf8;

-- ----------------------------
-- 4、 存储 cron trigger，包括 cron 表达式和时区信息
-- ----------------------------
drop table if exists qrtz_cron_triggers;
create table qrtz_cron_triggers (
    sched_name           varchar(120)    not null,
    trigger_name         varchar(200)    not null,
    trigger_group        varchar(200)    not null,
    cron_expression      varchar(200)    not null,
    time_zone_id         varchar(80),
    primary key (sched_name,trigger_name,trigger_group),
    foreign key (sched_name,trigger_name,trigger_group) references qrtz_triggers(sched_name,trigger_name,trigger_group)
) engine=innodb default charset=utf8;

-- ----------------------------
-- 5、 trigger 作为 blob 类型存储(用于 quartz 用户用 jdbc 创建他们自己定制的 trigger 类型，jobstore 并不知道如何存储实例的时候)
-- ----------------------------
drop table if exists qrtz_blob_triggers;
create table qrtz_blob_triggers (
    sched_name           varchar(120)    not null,
    trigger_name         varchar(200)    not null,
    trigger_group        varchar(200)    not null,
    blob_data            blob            null,
    primary key (sched_name,trigger_name,trigger_group),
    foreign key (sched_name,trigger_name,trigger_group) references qrtz_triggers(sched_name,trigger_name,trigger_group)
) engine=innodb default charset=utf8;

-- ----------------------------
-- 6、 以 blob 类型存储存放日历信息， quartz可配置一个日历来指定一个时间范围
-- ----------------------------
drop table if exists qrtz_calendars;
create table qrtz_calendars (
    sched_name           varchar(120)    not null,
    calendar_name        varchar(200)    not null,
    calendar             blob            not null,
    primary key (sched_name,calendar_name)
) engine=innodb default charset=utf8;

-- ----------------------------
-- 7、 存储已暂停的 trigger 组的信息
-- ----------------------------
drop table if exists qrtz_paused_trigger_grps;
create table qrtz_paused_trigger_grps (
    sched_name           varchar(120)    not null,
    trigger_group        varchar(200)    not null,
    primary key (sched_name,trigger_group)
) engine=innodb default charset=utf8;

-- ----------------------------
-- 8、 存储与已触发的 trigger 相关的状态信息，以及相联 job 的执行信息
-- ----------------------------
drop table if exists qrtz_fired_triggers;
create table qrtz_fired_triggers (
    sched_name           varchar(120)    not null,
    entry_id             varchar(95)     not null,
    trigger_name         varchar(200)    not null,
    trigger_group        varchar(200)    not null,
    instance_name        varchar(200)    not null,
    fired_time           bigint(13)      not null,
    sched_time           bigint(13)      not null,
    priority             integer         not null,
    state                varchar(16)     not null,
    job_name             varchar(200)    null,
    job_group            varchar(200)    null,
    is_nonconcurrent     varchar(1)      null,
    requests_recovery    varchar(1)      null,
    primary key (sched_name,entry_id)
) engine=innodb default charset=utf8;

-- ----------------------------
-- 9、 存储少量的有关 scheduler 的状态信息，假如是用于集群中，可以看到其他的 scheduler 实例
-- ----------------------------
drop table if exists qrtz_scheduler_state;
create table qrtz_scheduler_state (
    sched_name           varchar(120)    not null,
    instance_name        varchar(200)    not null,
    last_checkin_time    bigint(13)      not null,
    checkin_interval     bigint(13)      not null,
    primary key (sched_name,instance_name)
) engine=innodb default charset=utf8;

-- ----------------------------
-- 10、 存储程序的悲观锁的信息(假如使用了悲观锁)
-- ----------------------------
drop table if exists qrtz_locks;
create table qrtz_locks (
    sched_name           varchar(120)    not null,
    lock_name            varchar(40)     not null,
    primary key (sched_name,lock_name)
) engine=innodb default charset=utf8;

drop table if exists qrtz_simprop_triggers;
create table qrtz_simprop_triggers (
    sched_name           varchar(120)    not null,
    trigger_name         varchar(200)    not null,
    trigger_group        varchar(200)    not null,
    str_prop_1           varchar(512)    null,
    str_prop_2           varchar(512)    null,
    str_prop_3           varchar(512)    null,
    int_prop_1           int             null,
    int_prop_2           int             null,
    long_prop_1          bigint          null,
    long_prop_2          bigint          null,
    dec_prop_1           numeric(13,4)   null,
    dec_prop_2           numeric(13,4)   null,
    bool_prop_1          varchar(1)      null,
    bool_prop_2          varchar(1)      null,
    primary key (sched_name,trigger_name,trigger_group),
    foreign key (sched_name,trigger_name,trigger_group) references qrtz_triggers(sched_name,trigger_name,trigger_group)
) engine=innodb default charset=utf8;

commit;