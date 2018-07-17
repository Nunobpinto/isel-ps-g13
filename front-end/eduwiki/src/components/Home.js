import React from 'react'
import { Card, Col, Row } from 'antd'
import Login from './Login'
import { Link } from 'react-router-dom'
const { Meta } = Card

export default (props) => {
  return (
    <div id='holder'>
      <img alt='EduWiki Logo' id='home-logo' src='logo_color.png' />
      <div>
        <Login />
        <Row>
          <Col span={5}>
            <Card
              style={{ width: 240 }}
              cover={<img alt='Academic Resources' src='http://www.lourdes.edu/wp-content/uploads/2015/12/EnglishStudies-913x513.jpg' />}
            >
              <Meta
                title='Manage Academic Resources'
                description='Here you can check classes, exams, lectures, academic schedule and more'
              />
            </Card>
          </Col>
          <Col span={5}>
            <Card
              style={{ width: 240 }}
              cover={<img alt='Community Driven' src='http://stagtv.co.uk/wp-content/uploads/2017/07/group-of-users-silhouette_318-49953.jpg' />}
            >
              <Meta
                title='Community Driven'
                description='Manage whole data in community, the more you interact, higher your reputation'
              />
            </Card>
          </Col>
          <Col span={5}>
            <Card
              style={{ width: 240 }}
              cover={<img alt='Report' src='https://cdn0.iconfinder.com/data/icons/education-vol-02-2/32/report-student-grade-wrong-paper-exam-result-512.png' />}
            >
              <Meta
                title='Report Information'
                description='If something isn´t right you may report so that admin users will evaluate it'
              />
            </Card>
          </Col>
          <Col span={5}>
            <Card
              style={{ width: 240 }}
              cover={<img alt='Academic Email' src='https://fthmb.tqn.com/X7zpxW-USzMwCySRa3JZhbO3vVM=/768x0/filters:no_upscale():max_bytes(150000):strip_icc()/768px-Email_Shiny_Icon.svg-57fd8b3a3df78c690f82ca98.png' />}
            >
              <Meta
                title='Use academic Email'
                description='Use your own academic email to authenticate yourself as a University Student'
              />
            </Card>
          </Col>
        </Row>
      </div>
      <h1>Current web pages</h1>
      <ul>
        <li>
          <Link to={{pathname: '/organization'}}>Organization</Link>
        </li>
        <li>
          <Link to={{pathname: '/programmes'}}>Programmes</Link>
        </li>
        <li>
          <Link to={{pathname: '/courses'}}>Courses</Link>
        </li>
      </ul>
    </div>
  )
}
