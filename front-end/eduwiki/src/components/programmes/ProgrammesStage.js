import React from 'react'
import fetcher from '../../fetcher'
import {Input, List, message} from 'antd'
import IconText from '../comms/IconText'
import timestampParser from '../../timestampParser'

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
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(stagedUri, options)
      .then(programme => {
        message.success('Successfully approved staged programme')
        this.setState(prevState => {
          const newArray = prevState.staged.filter(programme => programme.stagedId !== prevState.stageID)
          return ({
            staged: newArray,
            viewStaged: newArray,
            approveStage: false
          })
        })
      })
      .catch(_ => {
        message.error('Cannot approve this staged programme')
        this.setState({approveStage: false})
      })
  }
  deleteStaged () {
    const stagedUri = `http://localhost:8080/programmes/stage/${this.state.stageID}`
    const options = {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(stagedUri, options)
      .then(_ => {
        message.success('Successfully deleted staged programme')
        this.setState(prevState => {
          const newArray = prevState.staged.filter(programme => programme.stagedId !== prevState.stageID)
          return ({
            staged: newArray,
            viewStaged: newArray,
            deleteStage: false
          })
        })
      })
      .catch(_ => {
        message.error('Cannot delete this staged programme')
        this.setState({deleteStage: false})
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
              <p>Created by: <a href={`/users/${item.createdBy}`}>{item.createdBy}</a></p>
              <p>Created at : {timestampParser(item.timestamp)}</p>
              <p>Votes: {item.votes}</p>
              <IconText
                type='check'
                onClick={() =>
                  this.setState({
                    approveStage: true,
                    stageID: item.stagedId
                  })}
                text='Approve staged Programme'
              />
              <IconText
                type='close'
                onClick={() =>
                  this.setState({
                    deleteStage: true,
                    stageID: item.stagedId
                  })}
                text='Delete staged Programme'
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
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    const stagedUri = 'http://localhost:8080/programmes/stage'
    fetcher(stagedUri, options)
      .then(json => {
        let staged = json.programmeStageList
        staged = staged.filter(st => st.createdBy !== this.props.username)
        this.setState({
          staged: staged,
          viewStaged: staged
        })
      })
      .catch(_ => message.error('Something bad happened'))
  }
  componentDidUpdate () {
    if (this.state.approveStage) {
      this.approveStaged()
    } else if (this.state.deleteStage) {
      this.deleteStaged()
    }
  }
}
