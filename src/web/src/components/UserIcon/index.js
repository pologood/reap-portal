import React from 'react';
import { Icon, Popover, Card, Modal } from 'antd';
import styles from './index.less';

const { Meta } = Card;
const { confirm } = Modal;

export default class UserIcon extends React.PureComponent {
  getUserCenter = () => {
    const { user, handleUserSetting } = this.props;
    const setting = <Icon type="setting" title="设置" onClick={handleUserSetting} />;
    const logout = <Icon type="logout" title="登出" onClick={this.handleLogout} />;
    // const avatar = <Avatar src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png" />;
    return (
      <Card actions={[setting, logout]}>
        <Meta title={`${user.username}/${user.name}`} description={user.org.name} />
      </Card>
    );
  };

  handleLogout = () => {
    const { handleLogout } = this.props;
    confirm({
      title: '确定登出?',
      mask: true,
      onOk() {
        handleLogout();
      },
    });
    this.visibleChange();
  };

  visibleChange = () => {
    this.props.triggerUserMenu();
  };

  render() {
    const { selected } = this.props;
    return (
      <Popover
        content={this.getUserCenter()}
        placement="bottomRight"
        popupClassName={styles.popover}
        trigger="click"
        visible={selected !== null}
        onVisibleChange={this.visibleChange}
        arrowPointAtCenter
      >
        <Icon type="user" className={selected} />
      </Popover>
    );
  }
}
