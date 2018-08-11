import React from 'react'
import fetch from 'isomorphic-fetch'
import {Input, List, message} from 'antd'
import IconText from '../comms/IconText'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      staged: [],
      viewStaged: []
    }
    this.filterStagedByName = this.filterStagedByName.bind(this)
    this.approveStaged = this.approveStaged.bind(this)
    this.deleteStaged = this.deleteStaged.bind(this)
  }
  filterStagedByName (ev) {
    const name = ev.target.value.toLowerCase()
    this.setState(prevState => {
      let array = [...prevState.staged]
      array = array.filter(programme => programme.fullName.toLowerCase().includes(name))
      return ({viewStaged: array})
    })
  }
  approveStaged () {
    const stagedUri = `http://localhost:8080/programmes/stage/${this.state.stageID}`
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth')
      }
    }
    fetch(stagedUri, options)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('error!!!')
        }
        return resp.json()
      })
      .then(programme => {
        this.props.history.push('/programmes/' + programme.programmeId)
      })
  }
  deleteStaged () {
    const stagedUri = `http://localhost:8080/programmes/stage/${this.state.stageID}`
    const options = {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth')
      }
    }
    fetch(stagedUri, options)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('error!!!')
        }
        this.setState(prevState => {
          const newArray = prevState.staged.filter(programme => programme.stageId !== prevState.stageID)
          return ({
            staged: newArray,
            viewStaged: newArray,
            deleteStage: false
          })
        })
      })
      .then(programme => {
        this.props.history.push('/programmes/' + programme.programmeId)
      })
  }
  render () {
    return (
      <div>
        <h1>All staged programmes</h1>
        <p> Filter By Name : </p>
        <Input.Search
          name='stagedNameFilter'
          placeholder='Search name'
          onChange={this.filterStagedByName}
        />
        <List id='staged-list'
          itemLayout='vertical'
          bordered
          dataSource={this.state.viewStaged}
          renderItem={item => (
            <List.Item>
              <h1>{`${item.fullName} (${item.shortName})`}</h1>
              <p>Academic degree: {item.academicDegree}</p>
              <p>Total Credits: {item.totalCredits}</p>
              <p>Duration: {item.duration}</p>
              <p>Created by: {item.createdBy}</p>
              <IconText
                type='check'
                onClick={() =>
                  this.setState({
                    approveStage: true,
                    stageID: item.stageId
                  })}
                text={item.votes}
              />
              <IconText
                type='close'
                onClick={() =>
                  this.setState({
                    deleteStage: true,
                    stageID: item.stageId
                  })}
                text={item.votes}
              />
            </List.Item>
          )}
        />
      </div>
    )
  }
  componentDidMount () {
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth')
      }
    }
    const stagedUri = 'http://localhost:8080/programmes/stage'
    fetch(stagedUri, options)
      .then(response => {
        if (response.status >= 400) {
          throw new Error('Unable to access content')
        }
        const ct = response.headers.get('content-type') || ''
        if (ct === 'application/json' || ct.startsWith('application/json;')) {
          return response.json()
        }
        throw new Error(`unexpected content type ${ct}`)
      })
      .then(json => this.setState({
        staged: json.programmeStageList,
        viewStaged: json.programmeStageList
      }))
      .catch(stagedError => message.error('Something bad happened'))
  }
  componentDidUpdate () {
    if (this.state.approveStage) {
      this.approveStaged()
    } else if (this.state.deleteStage) {
      this.deleteStaged()
    }
  }
}
