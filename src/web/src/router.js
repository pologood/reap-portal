import React from 'react';
import { routerRedux, Route, Switch } from 'dva/router';
import { LocaleProvider, Spin } from 'antd';
import zhCN from 'antd/lib/locale-provider/zh_CN';
import dynamic from 'dva/dynamic';
import { getRouterData } from './common/router';
import REAPPO0001 from './routes/REAPPO0001';
import styles from './index.less';

const { ConnectedRouter } = routerRedux;
dynamic.setDefaultLoadingComponent(() => {
  return <Spin size="large" className={styles.globalSpin} />;
});

function RouterConfig({ history, app }) {
  const routerData = getRouterData(app);
  const LoginLayout = routerData['/login'].component;
  const WelcomeLayout = routerData['/REAPPO0000'].component;
  const MenuLayout = routerData['/REAPPO0001'].component;
  const MainLayout = routerData['/'].component;

  return (
    <LocaleProvider locale={zhCN}>
      <ConnectedRouter history={history}>
        <Switch>
          <Route path="/login" component={LoginLayout} />
          <Route path="/REAPPO0000" render={props => <WelcomeLayout {...props} />} />
          <Route path="/REAPPO0001" render={props => <MenuLayout {...props} component={REAPPO0001} />} />
          <Route path="/" component={MainLayout} />
        </Switch>
      </ConnectedRouter>
    </LocaleProvider>
  );
}

export default RouterConfig;
