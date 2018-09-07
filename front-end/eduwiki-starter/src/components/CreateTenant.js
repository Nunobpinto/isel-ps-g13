import React from 'react'
import TenantForm from './TenanntForm'
import Layout from './Layout'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      submitted: false
    }
  }
  render () {
    return (
      <Layout history={this.props.history}>
        {this.state.submitted
          ? <h1>Now just wait until the devs reach you</h1>
          : <TenantForm successCb={() => this.setState({submitted: true})} />
        }
      </Layout>
    )
  }
}
