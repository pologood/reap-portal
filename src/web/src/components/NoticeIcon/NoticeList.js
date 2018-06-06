import React from 'react';
import { List, Avatar } from 'antd';
import classNames from 'classnames';
import Ellipsis from '../Ellipsis';
import styles from './NoticeList.less';

export default function NoticeList({ data = [], title }) {
  if (data.length === 0) {
    return (
      <div className={styles.notFound}>
        <div>暂无数据</div>
      </div>
    );
  }
  return (
    <div>
      <List className={styles.list}>
        {data.map((item, i) => {
          const itemCls = classNames(styles.item, {
            [styles.read]: item.read,
          });
          return (
            <List.Item className={itemCls} key={item.key || i}>
              <List.Item.Meta
                className={styles.meta}
                avatar={item.avatar ? <Avatar className={styles.avatar} src={item.avatar} /> : null}
                title={
                  <div className={styles.title}>
                    {item.title}
                    <div className={styles.extra}>{item.extra}</div>
                  </div>
                }
                description={
                  <div>
                    <div className={styles.description} title={item.description}>
                      <Ellipsis lines={2}>{item.description}</Ellipsis>
                    </div>
                    <div className={styles.datetime}>{item.datetime}</div>
                  </div>
                }
              />
            </List.Item>
          );
        })}
      </List>
      <div className={styles.clear}>
        清空
        {title}
      </div>
    </div>
  );
}
