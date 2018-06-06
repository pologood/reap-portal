import React from 'react';
import { Form, Select, Input, Row, Col, Modal } from 'antd';

const FormItem = Form.Item;
const { Option } = Select;

const Component = ({ form, visible, rowData, dispatch, parentMenuList, functions, selected }) => {
  // 查询功能码下拉列表
  const funcCodeOptions = [];
  const queryFuncCodeList = () => {
    if (funcCodeOptions.length === 0) {
      for (let i = 0; i < functions.length; i += 1) {
        funcCodeOptions.push(
          <Option value={functions[i].code}>
            {functions[i].code}-{functions[i].name}
          </Option>
        );
      }
    }
  };

  const handleOk = () => {
    form.validateFields((err, values) => {
      if (!err) {
        dispatch({
          type: 'REAPPO0001/create',
          menu: values,
        });
        form.resetFields();
      }
    });
  };

  const afterClose = () => {
    dispatch({
      type: 'REAPPO0001/setState',
      editModalVisiable: false,
    });
    form.resetFields();
  };

  const formItemLayout = {
    labelCol: { span: 8 },
    wrapperCol: { span: 16 },
  };

  const getParentMenuList = (m, level) => {
    m.map(item => {
      if (item.level < level && item.children) {
        getParentMenuList(item.children, level);
      }
      if (item.level === level && item.leaf === 'N') {
        parentMenuList.push(item);
      }
      return null;
    });
  };

  return (
    <Modal title="新增菜单" okText="保存" cancelText="取消" onOk={handleOk} onCancel={afterClose} visible={visible} afterClose={afterClose}>
      <Form>
        <Row>
          {selected ? (
            <Col>
              <FormItem {...formItemLayout} label="父菜单">
                <Input value={selected.name} readOnly />
              </FormItem>
            </Col>
          ) : null}

          <Col>
            <FormItem {...formItemLayout} label="菜单名称">
              {form.getFieldDecorator('name', { initialValue: rowData && rowData.name, rules: [{ required: true, message: '菜单名称必输' }] })(
                <Input placeholder="请输入菜单名称" />
              )}
            </FormItem>
          </Col>
          <Col>
            <FormItem {...formItemLayout} label="叶子节点">
              {form.getFieldDecorator('leaf', { initialValue: rowData && rowData.leaf, rules: [{ required: true, message: '请选择是否叶子节点' }] })(
                <Select style={{ width: 120 }} allowClear true>
                  <Option value="Y">是</Option>
                  <Option value="N">否</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          <Col>
            <FormItem {...formItemLayout} label="功能码" style={{ display: form.getFieldValue('leaf') === 'Y' ? 'block' : 'none' }}>
              {form.getFieldDecorator('functionCode', { rules: [{ required: form.getFieldValue('leaf') === 'Y', message: '功能码必输' }] })(
                <Select placeholder="请选择" onMouseEnter={queryFuncCodeList} style={{ width: 300 }}>
                  {funcCodeOptions}
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};
export default Form.create()(Component);
