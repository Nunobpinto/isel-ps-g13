import React from 'react'
import fetcher from '../../fetcher'
import {Card} from 'antd'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      classes: [],
      error: 'Follow a course',
      loadingClasses: true
    }
  }
  render () {
    return (
      <div className='side_item'>
        <Card
          title='My Classes'
          loading={this.state.loadingClasses}
          actions={[
            <p onClick={() => this.props.history.push('/classes')}>
                    See all classes
            </p>
          ]}
        >
          {this.state.classes.length === 0
            ? <p>Try following a class or try later</p>
            : <ul>
              {this.state.classes.map(courseClass => (
                <a href={`/classes/${courseClass.classId}/courses/${courseClass.courseId}`}>
                  <li>{courseClass.lecturedTerm}
                        /{courseClass.className}
                        /{courseClass.courseShortName}
                  </li>
                </a>
              ))}
            </ul>
          }
        </Card>
      </div>
    )
  }
  componentDidMount () {
    const authCookie = this.props.auth
    const options = {
      headers: {
        'Authorization': 'Basic ' + authCookie,
        'Access-Control-Allow-Origin': '*',
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(config.API_PATH + '/user/classes', options)
      .then(json => this.setState({
        classes: json.courseClassList,
        loadingClasses: false,
        error: undefined
      }))
      .catch(_ => this.setState({
        error: 'Try following a class or try later',
        loadingClasses: false
      }))
  }
}
