export default [
  {
    name: '数据连接',
    path: 'connector',
    children: [
      {
        name: '管理连接',
        path: 'management',
      },
      {
        name: '管理元数据',
        path: 'metadata',
      },
    ],
  },
  {
    name: '数据探索',
    path: 'exploration',
  },
  {
    name: '工作空间',
    path: 'workspace',
  },
];
