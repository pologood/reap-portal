import React from 'react';
import { Modal, Tabs, Select, Transfer, Form } from 'antd';
import { defaultHomeFunction } from '../../utils/function';
import styles from './index.less';

const FormItem = Form.Item;
const { Option } = Select;
const { TabPane } = Tabs;

export default class UserSetting extends React.PureComponent {
  state = {
    selectedKeys: [],
  };

  handleCancel = () => {
    this.props.dispatch({ type: 'global/hiddenUserSetting' });
  };

  render() {
    const { dispatch, showUserSetting, session: { user, userSetting } } = this.props;
    const functions = user.roles.flatMap(r => r.functions).filter(f => f.type === 'M');
    const formItemLayout = {
      labelCol: {
        span: 4,
      },
      wrapperCol: {
        span: 6,
      },
    };
    return (
      <Modal title="用户设置" visible={showUserSetting} width={900} onCancel={this.handleCancel} footer={null} className={styles.settingContainer}>
        <Tabs defaultActiveKey="1" tabPosition="left" style={{ height: 500 }}>
          <TabPane tab="基础设置" key="1">
            <FormItem {...formItemLayout} label="自定义首页">
              <Select
                showSearch
                style={{ width: 480 }}
                placeholder="选择功能码"
                defaultValue={(userSetting && userSetting.homeFunctionCode) || defaultHomeFunction.code}
                optionFilterProp="name"
                onChange={value => {
                  dispatch({ type: 'login/setHomeFunction', homeFunctionCode: value });
                }}
                filterOption={(input, option) => `${option.props.dataRef.name}/${option.props.dataRef.code}`.indexOf(input) >= 0}
              >
                {[{ ...defaultHomeFunction, name: '默认首页' }, ...functions].map(f => (
                  <Option dataRef={f} key={f.id} value={f.code}>
                    {f.name}/{f.code}
                  </Option>
                ))}
              </Select>
            </FormItem>
          </TabPane>
          <TabPane tab="常用功能" key="2">
            <Transfer
              dataSource={functions.map(r => ({ key: r.code, title: `${r.code}/${r.name}`, description: r.remark }))}
              titles={['可分配', '已分配']}
              targetKeys={((userSetting && userSetting.favFunctions) || []).map(f => f.functionCode)}
              selectedKeys={this.state.selectedKeys}
              listStyle={{ width: 300, height: 500 }}
              render={item => item.title}
              onSelectChange={(sourceSelectedKeys, targetSelectedKeys) => {
                this.setState({ selectedKeys: [...sourceSelectedKeys, ...targetSelectedKeys] });
              }}
              onChange={nextTargetKeys => {
                dispatch({ type: 'login/setFavFunctions', favFunctions: nextTargetKeys });
              }}
            />
          </TabPane>
        </Tabs>
      </Modal>
    );
  }
}
