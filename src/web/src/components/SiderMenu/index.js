import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Layout, Menu } from 'antd';
import Ellipsis from '../Ellipsis';
import styles from './index.less';

const { Sider } = Layout;
const MenuItemGroup = Menu.ItemGroup;

const siderStyles = {
  position: 'absolute',
  top: '32px',
  maxHeight: 'calc(100vh - 62px)',
  minHeight: 'calc(100vh - 62px)',
  overflowY: 'auto',
};

const menuStyles = { padding: '16px 0', width: '100%' };
class SiderMenu extends PureComponent {
  openFunction = func => {
    this.props.dispatch({
      type: 'global/openFunction',
      function: func,
    });
  };

  render() {
    const { functions, activeFunction } = this.props;
    return (
      <Sider style={siderStyles} className={styles.sider}>
        <Menu
          theme="dark"
          mode="inline"
          style={menuStyles}
          defaultSelectedKeys={[`${activeFunction}`]}
          onClick={({ key }) => {
            this.openFunction(functions.find(f => `${f.function.id}` === key).function);
          }}
        >
          <MenuItemGroup title={<span>常用功能</span>}>
            {functions.map(f => (
              <Menu.Item key={f.function.id}>
                <Ellipsis length={15} tooltip>
                  {f.function.name}
                </Ellipsis>
              </Menu.Item>
            ))}
          </MenuItemGroup>
        </Menu>
      </Sider>
    );
  }
}

export default connect(({ global, login }) => {
  return {
    functions: (login.session.userSetting && login.session.userSetting.favFunctions) || [],
    activeFunction: global.activeFunction,
  };
})(SiderMenu);
