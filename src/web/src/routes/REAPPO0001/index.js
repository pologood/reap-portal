import React from 'react';
import moment from 'moment';
import { Tree, Form, Select, Input, Button, Table, Row, Col, Popconfirm, Icon, message } from 'antd';
import { connect } from 'dva';
import EditableCell from '../../components/EditableCell';
import MenuEdit from './MenuEdit';
import styles from './index.less';

const FormItem = Form.Item;
const { Option } = Select;
const { TreeNode } = Tree;

const Component = ({ dispatch, menus, page, loading, editModalVisiable, parentMenuList, functions, selected }) => {
  // 渲染菜单树
  const renderTreeNodes = (data, parent) => {
    if (!data) {
      return null;
    }
    return data.map(item => {
      if (item.childrens && item.childrens.length > 0) {
        return (
          <TreeNode title={item.name} key={item.id} dataRef={item} parentRef={parent}>
            {renderTreeNodes(item.childrens, item)}
          </TreeNode>
        );
      }
      return <TreeNode title={item.name} key={item.id} dataRef={item} parentRef={parent} icon={<Icon type="info-circle-o" />} />;
    });
  };

  const onCellChange = (key, dataIndex) => {
    return value => {
      const menu = page.content.find(o => o.id === key);
      menu[dataIndex] = value;
      dispatch({ type: 'REAPPO0001/update', menu });
    };
  };

  // 菜单列表
  const columns = [
    {
      title: '菜单名称',
      width: '20%',
      dataIndex: 'name',
      render: (text, record) => <EditableCell value={text} onChange={onCellChange(record.id, 'name')} />,
    },
    {
      title: '父菜单',
      width: '20%',
      dataIndex: 'parentName',
    },
    {
      title: '叶子节点',
      width: '10%',
      dataIndex: 'leaf',
      render: leaf => {
        if (leaf === 'Y') {
          return '是';
        }
        return '否';
      },
    },
    {
      title: '功能码',
      width: '20%',
      dataIndex: 'functionCode',
      render: (text, record) => (record.leaf === 'Y' ? <EditableCell value={text} onChange={onCellChange(record.id, 'functionCode')} /> : ''),
    },
    {
      title: '创建时间',
      width: '20%',
      dataIndex: 'createTime',
      render: text => (text ? moment(text).format('YYYY-MM-DD HH:mm:ss') : null),
    },
    {
      title: '操作',
      dataIndex: 'operation',
      render: (text, record) => {
        return page && page.content && page.content.length > 0 ? (
          <Popconfirm title="确认删除?" onConfirm={() => dispatch({ type: 'REAPPO0001/delete', id: record.id })}>
            <a href="#">删除</a>
          </Popconfirm>
        ) : null;
      },
    },
  ];

  const formItemLayout = {
    labelCol: { span: 8 },
    wrapperCol: { span: 16 },
  };

  return (
    <div className={styles.menuContainer}>
      <Row gutter={48}>
        <Col span={4}>
          <Col span={20} push={2}>
            <Tree
              defaultExpandedKeys={['root']}
              showLine
              showIcon
              draggable
              selectedKeys={[selected && `${selected.id}`]}
              onDrop={info => {
                if (info.dragNode.props.dataRef.leaf !== 'Y') {
                  message.warning('仅支持拖拽移动菜单叶子节点.');
                  return;
                }

                if (info.node.props.eventKey === 'root') {
                  message.warning('无效的目标位置.');
                  return;
                }
                const dropPos = info.node.props.pos.split('-');
                const position = info.dropPosition - Number(dropPos[dropPos.length - 1]);
                dispatch({
                  type: 'REAPPO0001/moveMenu',
                  dragMenuId: info.dragNode.props.dataRef.id,
                  targetMenuId: info.node.props.dataRef.id,
                  position,
                });
              }}
              onSelect={(selectedKeys, e) => {
                dispatch({ type: 'REAPPO0001/select', selected: e.selected ? e.selectedNodes[0].props.dataRef : null });
              }}
            >
              <TreeNode key="root" onSelect={() => dispatch({ type: 'REAPRO0001/menus', selected: null })} icon={<Icon type="home" />} title="菜单树">
                {renderTreeNodes(menus)}
              </TreeNode>
            </Tree>
          </Col>
        </Col>
        <Col span={18}>
          <Row>
            <Form layout="inline" style={{ padding: '20px' }}>
              <FormItem {...formItemLayout} label="菜单名称">
                <Input
                  placeholder="菜单名称"
                  onChange={e => {
                    const { value } = e.target;
                    dispatch({
                      type: 'REAPPO0001/setState',
                      search: {
                        name: value,
                      },
                    });
                  }}
                />
              </FormItem>
              <FormItem {...formItemLayout} label="功能码">
                <Input
                  placeholder="功能码"
                  onChange={e => {
                    const { value } = e.target;
                    dispatch({
                      type: 'REAPPO0001/setState',
                      search: {
                        functionCode: value,
                      },
                    });
                  }}
                />
              </FormItem>
              <FormItem {...formItemLayout} label="菜单层级">
                <Select
                  style={{ width: 120 }}
                  allowClear
                  true
                  placeholder="菜单层级"
                  onChange={value => {
                    dispatch({
                      type: 'REAPPO0001/setState',
                      search: {
                        level: value,
                      },
                    });
                  }}
                >
                  <Option value={1}>一级菜单</Option>
                  <Option value={2}>二级菜单</Option>
                  <Option value={3}>三级菜单</Option>
                  <Option value={4}>四级菜单</Option>
                  <Option value={5}>五级菜单</Option>
                </Select>
              </FormItem>
              <FormItem>
                <Row gutter={48}>
                  <Col span={6}>
                    <Button type="primary" onClick={() => dispatch({ type: 'REAPPO0001/menus' })}>
                      筛选
                    </Button>
                  </Col>
                  <Col span={6}>
                    {' '}
                    <Button>清除</Button>
                  </Col>
                  <Col span={6} />
                  <Col span={6} />
                </Row>
              </FormItem>
            </Form>
          </Row>
          <Row>
            <div style={{ padding: '20px' }}>
              <Button
                type="primary"
                onClick={() => {
                  dispatch({
                    type: 'REAPPO0001/setState',
                    editModalVisiable: true,
                  });
                }}
              >
                新增
              </Button>
              <Table
                bordered
                dataSource={page && page.content}
                rowKey="id"
                columns={columns}
                pagination={{
                  total: page && page.totalElements,
                  showTotal: total => `总数为 ${total} 个菜单`,
                  onChange: (number, size) =>
                    dispatch({
                      type: 'REAPPO0001/menus',
                      page: number - 1,
                      size,
                    }),
                }}
                loading={loading}
              />
            </div>
            <MenuEdit
              visible={editModalVisiable}
              selected={selected}
              dispatch={dispatch}
              menus={menus}
              parentMenuList={parentMenuList}
              functions={functions}
            />
          </Row>
        </Col>
      </Row>
    </div>
  );
};
export default connect(({ REAPPO0001 }) => ({ ...REAPPO0001 }))(Form.create()(Component));
