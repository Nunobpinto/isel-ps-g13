import React from 'react'
import fetcher from '../../fetcher'
import {Input, List, message} from 'antd'
import IconText from '../comms/IconText'
import timestampParser from '../../timestampParser'
import config from '../../config'

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
      array = array.filter(course => course.fullName.toLowerCase().includes(name))
      return ({viewStaged: array})
    })
  }
  approveStaged () {
    const stagedUri = `${config.API_PATH}/courses/stage/${this.state.stageID}`
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(stagedUri, options)
      .then(course => {
        message.success('Successfully approved staged course')
        this.setState(prevState => {
          const newArray = prevState.staged.filter(crs => crs.stagedId !== prevState.stageID)
          return ({
            staged: newArray,
            viewStaged: newArray,
            approveStage: false
          })
        })
      })
      .catch(_ => {
        message.error('Cannot approve this staged course')
        this.setState({approveStage: false})
      })
  }
  deleteStaged () {
    const stagedUri = `${config.API_PATH}/courses/stage/${this.state.stageID}`
    const options = {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(stagedUri, options)
      .then(_ => {
        message.success('Successfully deleted staged course')
        this.setState(prevState => {
          const newArray = prevState.staged.filter(crs => crs.stagedId !== prevState.stageID)
          return ({
            staged: newArray,
            viewStaged: newArray,
            deleteStage: false
          })
        })
      })
      .catch(_ => {
        message.error('Cannot delete this staged course')
        this.setState({deleteStage: false})
      })
  }
  render () {
    return (
      <div>
        <h1>All staged Courses</h1>
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
              <p>Created By: <a href={`/users/${item.createdBy}`}>{item.createdBy}</a></p>
              <p>Created At: {timestampParser(item.timestamp)}</p>
              <IconText
                type='check'
                onClick={() =>
                  this.setState({
                    approveStage: true,
                    stageID: item.stagedId
                  })}
                text={item.votes}
              />
              <IconText
                type='close'
                onClick={() =>
                  this.setState({
                    deleteStage: true,
                    stageID: item.stagedId
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
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    const stagedUri = config.API_PATH + '/courses/stage'
    fetcher(stagedUri, options)
      .then(json => {
        let staged = json.courseStageList
        staged = staged.filter(st => st.createdBy !== this.props.username)
        this.setState({
          staged: staged,
          viewStaged: staged
        })
      })
      .catch(_ => message.error('Error obtaining all staged courses'))
  }
  componentDidUpdate () {
    if (this.state.approveStage) {
      this.approveStaged()
    } else if (this.state.deleteStage) {
      this.deleteStaged()
    }
  }
}
