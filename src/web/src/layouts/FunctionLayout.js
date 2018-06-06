import React from 'react';
import { Layout } from 'antd';
import { EVENT_TYPE_UI_RESET_VIEW } from '../constants';

const { Content } = Layout;

class FunctionLayout extends React.PureComponent {
  render() {
    const Component = this.props.component;
    return (
      <Layout>
        <Layout>
          <Content style={{ margin: '24px 24px 0', height: '100%' }}>
            <div
              style={{
                minHeight: 'calc(100vh - 30px)',
                margin: '0 auto 0 auto',
                padding: '24px',
                backgroundColor: '#fbfbfb',
              }}
              onClick={() => {
                window.parent.postMessage({ type: EVENT_TYPE_UI_RESET_VIEW }, '*');
              }}
            >
              <Component {...this.props} />
            </div>
          </Content>
        </Layout>
      </Layout>
    );
  }
}

export default FunctionLayout;
