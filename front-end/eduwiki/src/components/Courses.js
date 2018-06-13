import React from 'react'
import fetch from 'isomorphic-fetch'
import {Link} from 'react-router-dom'
import Navbar from './Navbar'
import {Button, Input, Form, List} from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      courses: [],
      error: undefined
    }
  }

  render () {
    return (
      <div>
        <Navbar />
        {this.state.error ? <p>
              Error getting all the courses please try again !!!
        </p>
          : <div>
            <h1>All courses in ISEL</h1>
            <List
              bordered
              dataSource={this.state.courses}
              renderItem={item => (
                <List.Item>
                  <Link to={{pathname: `/courses/${item.id}`}}>
                    {item.fullName} ({item.shortName}) - Created By {item.createdBy}
                  </Link>
                </List.Item>
              )
              }
            />
          </div>
        }
      </div>
    )
  }

  componentDidMount () {
    const uri = 'http://localhost:8080/courses/'
    const header = {
      headers: { 'Access-Control-Allow-Origin': '*' }
    }
    fetch(uri, header)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        const ct = resp.headers.get('content-type') || ''
        if (ct === 'application/json' || ct.startsWith('application/json;')) {
          return resp.json()
        }
        throw new Error(`unexpected content type ${ct}`)
      })
      .then(json => {
        this.setState({courses: json})
      })
      .catch(error => {
        this.setState({
          error: error
        })
      })
  }
}
