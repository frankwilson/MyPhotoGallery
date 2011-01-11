INSERT INTO `Roles`
    (`id`, `name`, `created` )
VALUES
    ( 1, 'ROLE_USER', NOW() );

INSERT INTO `Users`
    (`id`, `login`, `password`, email, `created`, `blocked`, `temporary` )
VALUES
    ( 1, 'wilson', 'wilson', 'pv.kazantsev@gmail.com', now(), b'0', b'0' );

INSERT INTO `UserRoles`(`roleId`, `userId` ) VALUES( 1, 1 );