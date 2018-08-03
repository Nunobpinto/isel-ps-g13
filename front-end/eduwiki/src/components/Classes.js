import React from 'react'
import fetch from 'isomorphic-fetch'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      classes: []
    }
  }
  render () {

  }
  componentDidMount () {
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth')
      }
    }
    const classesUri = 'http://localhost:8080/classes'
    fetch(classesUri, options)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Error!!!!')
        }
        return resp.json()
      })
      .then(classes => {
         
      })
  }
}
