import React, { Component } from 'react';
import { Checkbox, Alert, Form, Input, Icon, Button, Row, Col } from 'antd';
import styles from './Login.less';
import logo from '../../assets/logo.svg';

const FormItem = Form.Item;
export default class LoginPage extends Component {
  renderMessage = content => {
    return <Alert style={{ marginBottom: 12 }} message={content} type="error" showIcon />;
  };

  render() {
    return (
      <div className={styles.main}>
        <Row type="flex" justify="end" className={styles.version} />
        <Row type="flex" justify="center">
          <img alt="logo" src={logo} width="100" height="100" style={{ marginBottom: 12 }} />
        </Row>
        <Row type="flex" justify="center">
          <Col span={18}>
            <Form>
              <FormItem>
                <Input name="username" prefix={<Icon type="user" />} placeholder="admin/user" />
              </FormItem>
              <FormItem>
                <Input name="password" prefix={<Icon type="lock" />} type="password" placeholder="888888/123456" />
              </FormItem>
              <div>
                <Checkbox checked>记住用户名</Checkbox>
              </div>
              <FormItem>
                <Button className={styles.submit} size="large" type="primary" htmlType="submit">
                  登录
                </Button>
              </FormItem>
            </Form>
          </Col>
        </Row>
      </div>
    );
  }
}
