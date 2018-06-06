import React from 'react';
import { Layout } from 'antd';
import { EVENT_TYPE_UI_RESET_VIEW } from '../constants';
import styles from './WelcomeLayout.less';

const { Content } = Layout;

export default class WelcomeLayout extends React.PureComponent {
  render() {
    return (
      <Layout>
        <Layout>
          <Content
            className={styles.welcome}
            onClick={() => {
              window.parent.postMessage({ type: EVENT_TYPE_UI_RESET_VIEW }, '*');
            }}
          />
        </Layout>
      </Layout>
    );
  }
}
