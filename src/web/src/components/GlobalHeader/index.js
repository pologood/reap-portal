import React, { PureComponent } from 'react';
import { Icon } from 'antd';
import { connect } from 'dva';
// import NoticeIcon from '../NoticeIcon';
import UserIcon from '../UserIcon';
import styles from './index.less';
import MainMenu from '../MainMenu';

@connect(({ login, global }) => ({
  user: login.session.user,
  mainMenu: global.ui.mainMenu,
  userMenu: global.ui.userMenu,
  functions: global.functions,
  activeFunction: global.activeFunction,
}))
export default class GlobalHeader extends PureComponent {
  handleLogout = () => {
    this.props.dispatch({
      type: 'login/changeLogoutStatus',
    });
    this.props.dispatch({
      type: 'global/changeLogoutStatus',
    });
  };

  handleUserSetting = () => {
    this.props.dispatch({
      type: 'global/showUserSetting',
    });
  };

  render() {
    const { functions, activeFunction, mainMenu, userMenu, triggerMainMenu, triggerUserMenu, triggerSideMenu, user } = this.props;
    const func = functions.find(f => f.id === activeFunction);
    return (
      <div className={styles.header}>
        {mainMenu ? <MainMenu /> : null}
        <Icon type="bars" onClick={triggerSideMenu} />
        <span className={styles.function}> {func ? `${func.code}/${func.name}` : null}</span>
        <div className={styles.right}>
          <Icon className={mainMenu ? styles.selected : null} type="bars" onClick={triggerMainMenu} />
          <UserIcon
            selected={userMenu ? styles.selected : null}
            user={user}
            triggerUserMenu={triggerUserMenu}
            handleLogout={this.handleLogout}
            handleUserSetting={this.handleUserSetting}
          />
        </div>
      </div>
    );
  }
}
