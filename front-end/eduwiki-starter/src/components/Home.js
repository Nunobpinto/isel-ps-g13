import React from 'react'
import Tenants from './Tenants'
import { Card, Col, Row } from 'antd'
import Layout from './Layout'

export default (props) => (
  <Layout history={props.history}>
    <div className='container'>
      <img alt='EduWiki Logo' id='home-logo' src='logo_color.png' />
      <div style={{ padding: '30px' }}>
        <Row gutter={16}>
          <Col span={8}>
            <Card title='Manage your Academic Information' bordered={false}>
                    Manage all courses, programmes, exams and so much more, all can be find here with quick access
            </Card>
          </Col>
          <Col span={8}>
            <Card title='Manage everything in Community' bordered={false}>
                    Live in community, upvoting the resources that you found useful and report everithing useless,
                    or just part of it
            </Card>
          </Col>
          <Col span={8}>
            <Card title='Earn Your Reputation' bordered={false}>
                    Make yourself important in your academic community, create useful resources for everyone!!
            </Card>
          </Col>
        </Row>
      </div>
      <Tenants history={props.history} />
    </div>
  </Layout>
)
