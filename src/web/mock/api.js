export default {
  'POST /api/REAP/login': ({ body, succ, fail }) => {
    if ((body.username === 'admin' && body.password === '888888')||(body.username === 'user' && body.password === '123456')) {
      succ({
        token: 'fakeToken',
        user: {
          id: 1,
          name: 'XX猫',
          username: '7upcat',
          email: '7upcat@gmail.com',
          status: 'A',
          phone: '18603020480',
          gender: 'M',
          org: {
            id: 1,
            orgName: '测试机构1',
          },
          userSetting: {
            id: 1,
            homeFunction: {
              id: 211,
              code: 'REAP0211',
              name: '子菜单211功能码很长很长',
              action: 'https://cn.bing.com/',
            },
            favFunctions: [
              {
                id: 3,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 4,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 5,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 6,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 7,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 8,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 9,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 10,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 11,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 12,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 13,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 14,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 15,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 16,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 17,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 18,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 19,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 20,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 21,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 22,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
              {
                id: 23,
                code: 'REAP0003',
                name: '子菜单3功能码',
                action: 'http://webo.com',
              },
            ],
          },
        },
        menus: [
          {
            id: 1,
            name: '基础信息维护',
            sequence: 1,
            childrens: [
              {
                id: 2,
                name: '机构信息维护',
                sequence: 1,
                function: {
                  id: 2112,
                  code: 'REAPFC0001',
                  name: '机构信息维护',
                  action: 'http://localhost:8000/#/REAPRB0001',
                },
              },
              {
                id: 3,
                name: '岗位信息维护',
                sequence: 2,
                function: {
                  id: 2113,
                  code: 'REAPFC0003',
                  name: '用户信息维护',
                  action: 'http://localhost:8000/#/REAPRB0003',
                },
              },
              {
                id: 4,
                name: '用户信息维护',
                sequence: 3,
                function: {
                  id: 2113,
                  code: 'REAPFC0002',
                  name: '用户信息维护',
                  action: 'http://localhost:8000/#/REAPRB0002',
                },
              },
              {
                id: 21,
                name: '子菜单21',
                sequence: 3,
                childrens: [
                  {
                    id: 211,
                    name: '子菜单211',
                    sequence: 1,
                    function: {
                      id: 211,
                      code: 'REAP0211',
                      name: '子菜单211功能码很长很长',
                      action: 'https://cn.bing.com/',
                    },
                  },
                ],
              },
            ],
          },
          {
            id: 3,
            name: '菜单2',
            sequence: 2,
            childrens: [
              {
                id: 4,
                name: '子菜单2',
                sequence: 1,
                function: {
                  id: 2,
                  code: 'REAP0002',
                  name: '子菜单2功能码',
                  action: 'https://cn.bing.com/',
                },
              },
            ],
          },
          {
            id: 4,
            name: '菜单3',
            sequence: 3,
            function: {
              id: 2,
              code: 'REAP0002',
              name: '子菜单2功能码非常非常的长',
              action: 'http://localhost:8000/index1.html',
            },
          },
          {
            id: 5,
            name: '菜单4',
            sequence: 4,
            function: {
              id: 3,
              code: 'REAP0003',
              name: '子菜单3功能码',
              action: 'http://webo.com',
            },
          },
        ],
      });
    } else {
      return fail('REAP010001', '用户或密码错误');
    }
  },
};
