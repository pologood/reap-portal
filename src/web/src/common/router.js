import { createElement } from 'react';
import dynamic from 'dva/dynamic';

const routers = {
  '/': {
    component: () => import('../layouts/MainLayout'),
    models: ['login'],
  },
  '/login': {
    component: () => import('../layouts/LoginLayout'),
    models: ['login'],
  },
  '/REAPPO0001': {
    component: () => import('../layouts/FunctionLayout'),
    models: ['REAPPO0001'],
  },
  '/REAPPO0000': {
    component: () => import('../layouts/WelcomeLayout'),
  },
};

let routerDataCache;

const modelNotExisted = (app, model) =>
  // eslint-disable-next-line
  !app._models.some(({ namespace }) => {
    return namespace === model.substring(model.lastIndexOf('/') + 1);
  });

// wrapper of dynamic
const dynamicWrapper = (app, models, component) => {
  // () => require('module')
  // transformed by babel-plugin-dynamic-import-node-sync
  if (component.toString().indexOf('.then(') < 0) {
    models.forEach(model => {
      if (modelNotExisted(app, model)) {
        console.log(model)
        // eslint-disable-next-line
        app.model(require(`../models/${model}`).default);
      }
    });
    return props => {
      if (!routerDataCache) {
        routerDataCache = getRouterData(app);
      }
      return createElement(component().default, {
        ...props,
        routerData: routerDataCache,
      });
    };
  }

  // () => import('module')
  return dynamic({
    app,
    models: () => {
      // models.filter(model => modelNotExisted(app, model)).map(m => import(frame ?`../models/${m}.js`:`../../models/${m}.js`));
      models.filter(model => modelNotExisted(app, model)).map(m => import(`../models/${m}.js`));
    },
    // add routerData prop
    component: () => {
      if (!routerDataCache) {
        routerDataCache = getRouterData(app);
      }
      return component().then(raw => {
        const Component = raw.default || raw;
        return props =>
          createElement(Component, {
            ...props,
            routerData: routerDataCache,
          });
      });
    },
  });
};

export const getRouterData = app => {
  const routerConfig = {};
  Object.keys(routers).forEach(key => {
    const { component, models, frame } = routers[key];
    routerConfig[key] = {
      component: dynamicWrapper(app, models || [], component, frame),
    };
  });

  // Route configuration data
  // eg. {name,authority ...routerConfig }
  const routerData = {};
  // The route matches the menu
  Object.keys(routerConfig).forEach(path => {
    let router = routerConfig[path];
    router = {
      ...router,
    };
    routerData[path] = router;
  });
  return routerData;
};
