import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Icon } from 'antd';
import classNames from 'classnames';
import styles from './index.less';

class MainMenu extends PureComponent {
  openFunction = func => {
    this.props.dispatch({ type: 'global/openFunction', function: func });
  };

  selectMenu = (select, level) => {
    this.props.dispatch({ type: 'global/selectMenu', select, level });
  };

  buildMenus = (menus, level, id) => {
    return (
      menus &&
      menus.map(m => {
        const svg = m.childrens && m.childrens.length > 0 ? <Icon type="caret-right" /> : null;
        const openTab = m.function ? () => this.openFunction(m.function) : null;
        const selectMenu = () => this.selectMenu(m, level);
        const cls = classNames(m.childrens && m.childrens.length > 0 ? styles.item : styles.leafItem, m.id === id ? styles.selected : null);
        return (
          <div className={cls} key={m.id} onClick={openTab} onMouseOver={selectMenu} onFocus={selectMenu}>
            {/* eslint-disable-next-line */}
            <label>{`${m.sequence}`.padStart(3, '0')}</label>
            <span id={`${m.id}`}>{m.name}</span>
            {svg}
          </div>
        );
      })
    );
  };

  render() {
    const menus = [this.props.menus].concat(this.props.mainMenus);
    const { selectMenus } = this.props;
    return (
      <div className={styles.menuView}>
        <div className={styles.menuCategory}>
          {Array(...Array(5))
            .map((v, index) => index)
            .map(v => <span key={v} />)}
        </div>
        <div className={styles.menuListContainer}>
          {menus.map((ms, index) => {
            /* eslint-disable */
            return (
              <div key={`${index}`} className={styles.menuList}>
                {this.buildMenus(ms, index, selectMenus[index])}
              </div>
            );
          })}
        </div>
      </div>
    );
  }
}

export default connect(({ global, login }) => ({
  menus: login.session.menus,
  mainMenus: global.mainMenus,
  selectMenus: global.selectMenus,
}))(MainMenu);
