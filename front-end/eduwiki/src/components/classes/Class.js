import React from 'react'
import fetcher from '../../fetcher'
import { Link } from 'react-router-dom'
import IconText from '../comms/IconText'
import Layout from '../layout/Layout'
import { Button, Row, Col, message, List, Card, Breadcrumb } from 'antd'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      klass: {},
      classCourses: []
    }
  }
  render () {
    return (
      <Layout>
        <div className='title_div'>
          <h1>
            <strong>Class {this.state.klass.lecturedTerm}</strong>
             /
            <strong>{this.state.klass.className}</strong>
          </h1>
        </div>
        <div className='version_div'>
            Version
        </div>
        <div style={{ padding: '20px' }}>
          <Row gutter={16}>
            <Col span={5}>
              <Card title='Created By'>
                {this.state.klass.createdBy}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Added At'>
                {this.state.klass.timestamp}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Votes'>
                {this.state.klass.votes}
              </Card>
            </Col>
          </Row>
          <h1>Courses</h1>
          <div style={{ padding: '30px' }}>
            <Row gutter={16}>
              {this.state.classCourses.map(item =>
                <Col span={8} key={item.courseId}>
                  <Card title={item.courseShortName}>
                    <p>Created by {item.createdBy}</p>
                    <p>Votes : {item.votes}</p>
                    <p>Added at {item.timestamp}</p>
                    <Link to={{pathname: `/classes/${item.classId}/courses/${item.courseId}`}}>See it's page</Link>
                  </Card>
                </Col>
              )}
            </Row>
          </div>
        </div>
        <div>
            Add courses to this class div
        </div>
      </Layout>
    )
  }
  componentDidMount () {
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/classes/' + id
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(klass => {
        const classCoursesUri = uri + '/courses'
        fetcher(classCoursesUri, header)
          .then(json => this.setState({
            klass: klass,
            classCourses: json.courseClassList
          }))
          .catch(_ => {
            message.error('Error fetching courses of this class')
            this.setState({klass: klass})
          })
      })
      .catch(error => {
        message.error('Error fetching class with id ' + id)
        this.setState({error: error})
      })
  }
}
