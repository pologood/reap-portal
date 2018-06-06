const config = () => {
  // 应用配置
  const configurations = {};
  if (process.env.NODE_ENV === 'development') {
    // eslint-disable-next-line
    Object.assign(configurations, require('../mock/devConfig').default);
  }
  return configurations;
};

export default config();
