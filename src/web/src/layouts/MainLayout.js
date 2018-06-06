import React from 'react';
import { Redirect } from 'react-router-dom';
import { Layout } from 'antd';
import { connect } from 'dva';
import GlobalHeader from '../components/GlobalHeader';
import TabContent from '../components/TabContent';
import SiderMenu from '../components/SiderMenu';
import UserSetting from '../components/UserSetting';
import styles from './MainLayout.less';

const { Content, Header } = Layout;

@connect(({ global, login }) => ({ ui: global.ui, session: login.session }))
export default class MainLayout extends React.PureComponent {
  triggerMainMenu = () => {
    this.props.dispatch({ type: 'global/triggerMainMenu' });
  };

  triggerUserMenu = () => {
    this.props.dispatch({ type: 'global/triggerUserMenu' });
  };

  triggerSideMenu = () => {
    this.props.dispatch({ type: 'global/triggerSideMenu' });
  };

  render() {
    const { dispatch, session, ui: { sideMenu, showUserSetting } } = this.props;
    if (session.user) {
      const side = sideMenu ? <SiderMenu /> : null;
      return (
        <Layout>
          {side}
          <Layout>
            <Header style={{ padding: 0 }} className={styles.header}>
              <GlobalHeader triggerSideMenu={this.triggerSideMenu} triggerMainMenu={this.triggerMainMenu} triggerUserMenu={this.triggerUserMenu} />
            </Header>
            <Content className={styles.content} onClick={this.hidden}>
              <UserSetting session={session} showUserSetting={showUserSetting} dispatch={dispatch} />
              <TabContent />
            </Content>
          </Layout>
        </Layout>
      );
    } else {
      return (
        <Redirect
          to={{
            pathname: '/login',
          }}
        />
      );
    }
  }
}
