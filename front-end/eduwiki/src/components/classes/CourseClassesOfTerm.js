import React from 'react'
import fetcher from '../../fetcher'
import Cookies from 'universal-cookie'
import {message, Tooltip, Button, Col, Row, Card} from 'antd'
import {Link} from 'react-router-dom'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      classes: props.classes,
      courseBeingFollowed: props.courseBeingFollowed
    }
  }
  render () {
    return (
      <div style={{ padding: '30px' }}>
        <Row gutter={16}>
          {this.state.classes.map(item =>
            <Col span={8} key={item.classId}>
              <Card title={item.className}>
                <p>Created by {item.createdBy}</p>
                <p>Votes : {item.votes}</p>
                <p>Added at {item.timestamp}</p>
                <Link to={{pathname: `/classes/${item.classId}/courses/${item.courseId}`}}>See it's page</Link>
                {this.state.courseBeingFollowed &&
                <p>
                  {item.folowing
                    ? <Tooltip placement='bottom' title={'Unfollow this class'}>
                      <Button id='report_btn' icon='close-circle' onClick={() => this.setState({
                        unFollowFlag: true,
                        classId: item.classId
                      })} />
                    </Tooltip>
                    : <Tooltip placement='bottom' title={'Follow this class'}>
                      <Button id='report_btn' icon='heart' onClick={() => this.setState({
                        followFlag: true,
                        classId: item.classId
                      })} />
                    </Tooltip>
                  }
                </p>
                }
              </Card>
            </Col>
          )}
        </Row>
      </div>
    )
  }
  componentDidUpdate () {
    if (this.state.followFlag) {

    } else if (this.state.unFollowFlag) {

    }
  }
  componentDidMount () {
    const uri = 'http://localhost:8080/user/classes'
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, options)
      .then(json => {
        let classesOfUser = json.classList
        this.setState(prevState => {
          const classesOfCourse = prevState.classes
          let followedCourses = classesOfCourse.filter(cls => classesOfUser.findIndex(userCls =>
            cls.classId === userCls.classId
          ) !== -1)
          let classes = classesOfCourse.map(cls => {
            const idx = followedCourses.findIndex(followedCls => followedCls.classId === cls.classId)
            if (idx !== -1) {
              cls['folowing'] = true
            }
            return cls
          })
          return ({classes: classes})
        })
      })
      .catch(_ => message.error('Error fetching user classes'))
  }
}
