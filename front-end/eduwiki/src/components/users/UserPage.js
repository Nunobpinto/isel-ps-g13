import React from 'react'
import UserProgramme from './UserProgramme'
import UserCourses from './UserCourses'
import UserOrganization from './UserOrganization'
import Feed from './Feed'
import UserClasses from './UserClasses'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      classes: undefined
    }
  }
  render () {
    return (
      <div>
        <div className='left_side'>
          <UserProgramme
            auth={this.props.auth}
            history={this.props.history} />
          <UserCourses
            auth={this.props.auth}
            history={this.props.history} />
          <UserClasses
            auth={this.props.auth}
            history={this.props.history} />
        </div>
        <div className='centre_div'>
          <h1>Feed</h1>
          <Feed />
        </div>
        <div className='right_side'>
          <UserOrganization
            auth={this.props.auth}
            history={this.props.history} />
        </div>
      </div>
    )
  }
}
