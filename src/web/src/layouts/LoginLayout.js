import React from 'react';
import { Alert, Form, Input, Icon, Button, Row, Col } from 'antd';
import { Redirect } from 'react-router-dom';
import { connect } from 'dva';
import styles from './LoginLayout.less';
import logo from '../assets/logo.png';

const FormItem = Form.Item;

@connect(({ login }) => ({ session: login.session, result: login.result }))
class UserLayout extends React.PureComponent {
  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        this.props.dispatch({ type: 'login/login', payload: values });
      }
    });
  };

  renderMessage = content => {
    return (
      <Alert
        style={{
          marginBottom: 12,
        }}
        message={content}
        type="error"
        showIcon
      />
    );
  };

  render() {
    const { session, result } = this.props;
    const { getFieldDecorator } = this.props.form;
    return session.user ? (
      <Redirect
        to={{
          pathname: '/',
        }}
      />
    ) : (
      <div className={styles.container}>
        <div className={styles.main}>
          <Row type="flex" justify="end" className={styles.version} />
          <Row type="flex" justify="center">
            <img
              alt="logo"
              src={logo}
              style={{
                marginBottom: 12,
              }}
            />
          </Row>
          <Row type="flex" justify="center">
            <Col span={18}>
              {result && !result.success ? this.renderMessage(`${result.responseMessage}`) : null}
              <Form onSubmit={this.handleSubmit}>
                <FormItem>
                  {getFieldDecorator('username', {
                    rules: [
                      {
                        required: true,
                        message: '请输入用户名!',
                      },
                    ],
                  })(<Input prefix={<Icon type="user" />} placeholder="admin" />)}
                </FormItem>
                <FormItem>
                  {getFieldDecorator('password', {
                    rules: [
                      {
                        required: true,
                        message: '请输入密码!',
                      },
                    ],
                  })(<Input prefix={<Icon type="lock" />} type="password" placeholder="888888" />)}
                </FormItem>
                <FormItem>
                  <Button className={styles.submit} size="large" type="primary" htmlType="submit">
                    登录
                  </Button>
                </FormItem>
              </Form>
            </Col>
          </Row>
        </div>
      </div>
    );
  }
}

export default Form.create()(UserLayout);
