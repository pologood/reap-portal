import React from 'react';
import { Icon, Badge, Popover, Tabs } from 'antd';
import List from './NoticeList';
import styles from './index.less';

const { TabPane } = Tabs;

export default class NoticeIcon extends React.PureComponent {
  getNotificationBox() {
    return (
      <Tabs className={styles.tabs} onChange={this.onTabChange} activeKey="111">
        <TabPane tab="任务" key="111">
          <List
            data={[
              {
                title: '待办任务1',
                extra: '过期',
                description:
                  '这是任务一的任务描述非常长非常长非常长,这是任务一的任务描述非常长非常长非常长,这是任务一的任务描述非常长非常长非常长,这是任务一的任务描述非常长非常长非常长',
                datetime: '9个月前',
                avatar: 'https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png',
                read: false,
              },
            ]}
            title="任务"
          />
        </TabPane>
        <TabPane tab="消息" key="222">
          <List
            data={[
              {
                title: '待办任务1',
                extra: '过期',
                description:
                  '这是任务一的任务描述非常长非常长非常长,这是任务一的任务描述非常长非常长非常长,这是任务一的任务描述非常长非常长非常长,这是任务一的任务描述非常长非常长非常长',
                datetime: '9个月前',
                avatar: 'https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png',
                read: false,
              },
            ]}
            title="任务"
          />
        </TabPane>
      </Tabs>
    );
  }

  render() {
    return (
      <Popover content={this.getNotificationBox()} placement="bottomRight" popupClassName={styles.popover} trigger="click" arrowPointAtCenter>
        <Badge className={styles.badge} count={10}>
          <Icon type="bell" className={styles.notice} />
        </Badge>
      </Popover>
    );
  }
}
