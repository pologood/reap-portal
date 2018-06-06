import React from 'react';
import { Tabs, Icon, Spin } from 'antd';
import { connect } from 'dva';
import classNames from 'classnames';
import { EVENT_TYPE_UI_RESET_VIEW } from '../../constants';
import { resolveAction, defaultHomeFunction } from '../../utils/function';
import Ellipsis from '../Ellipsis';
import styles from './index.less';

const { TabPane } = Tabs;

@connect(({ global, login }) => ({
  loaded: global.ui.loaded,
  functions: global.functions,
  activeFunction: global.activeFunction,
  session: login.session,
}))
export default class TabContent extends React.PureComponent {
  state = {
    hiddenTab: false,
    activeFunction: null,
  };

  componentDidMount() {
    const { activeFunction } = this.props;
    setTimeout(() => {
      this.setState({ hiddenTab: true, activeFunction });
    }, 1000);
  }

  componentDidUpdate() {
    const { activeFunction } = this.props;
    if (activeFunction !== this.state.activeFunction) {
      setTimeout(() => {
        this.setState({ hiddenTab: false });
        setTimeout(() => this.setState({ hiddenTab: true, activeFunction }), 500);
      }, 10);
    }
  }

  closeCurrentTab = () => {
    this.props.dispatch({ type: 'global/closeFunction', id: `${this.props.activeFunction}` });
  };

  remove = targetKey => {
    this.props.dispatch({ type: 'global/closeFunction', id: targetKey });
  };

  changeTab = targetKey => {
    this.props.dispatch({ type: 'global/activeFunction', id: targetKey });
  };

  render() {
    const { activeFunction, functions, session: { userSetting }, loaded } = this.props;
    const { hiddenTab } = this.state;
    const homeFunction = (userSetting && userSetting.homeFunction) || defaultHomeFunction;
    // tabs 由用户设置的首页以及打开的功能码
    const tabs = [{ ...homeFunction, name: '首页', closable: false }].concat(functions.filter(f => !homeFunction || f.id !== homeFunction.id));
    const active = tabs.find(f => `${f.id}` === activeFunction) || homeFunction;
    return (
      <div className={styles.tabContent}>
        {active && active.closable ? <div className={styles.closeTab} title="Close" onClick={this.closeCurrentTab} /> : null}
        <Tabs
          className={classNames(styles.footerTabs, hiddenTab ? styles.hiddenTab : null)}
          activeKey={`${active && active.id}`}
          onChange={this.changeTab}
          type="line"
          tabPosition="bottom"
          hideAdd
          onEdit={this.onEdit}
        >
          {tabs.map((t, index) => {
            return (
              <TabPane
                tab={
                  <div>
                    <Ellipsis length={10} tooltip>
                      {t.name}
                    </Ellipsis>
                    {index !== 0 ? (
                      <Icon
                        type="close"
                        onClick={e => {
                          e.stopPropagation();
                          this.remove(`${t.id}`);
                        }}
                      />
                    ) : null}
                  </div>
                }
                key={`${t.id}`}
                closable={t.closable}
              >
                <Spin
                  tip="加载中..."
                  size="large"
                  indicator={<Icon type="loading" style={{ fontSize: 32 }} />}
                  spinning={!loaded.includes(t.id)}
                  wrapperClassName={styles.loading}
                  onClick={() => window.parent.postMessage({ type: EVENT_TYPE_UI_RESET_VIEW }, '*')}
                >
                  <iframe
                    id={`tab${t.id}`}
                    title={t.name}
                    className={styles.frame}
                    src={resolveAction(t)}
                    onLoad={() => {
                      this.props.dispatch({ type: 'global/loaded', id: t.id });
                    }}
                  />
                </Spin>
              </TabPane>
            );
          })}
        </Tabs>
      </div>
    );
  }
}
