import React from 'react'
import UserProgramme from './UserProgramme'
import UserCourses from './UserCourses'
import UserOrganization from './UserOrganization'

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
