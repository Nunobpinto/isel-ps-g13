import React from 'react'
import fetcher from '../../fetcher'
import { Link } from 'react-router-dom'
import IconText from '../comms/IconText'
import Layout from '../layout/Layout'
import { Button, Input, List, message } from 'antd'
import CreateProgramme from './CreateProgramme'
import timestampParser from '../../timestampParser'
import config from '../../config'

class Programmes extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      programmes: [],
      viewProgrammes: [],
      progError: undefined,
      full_name: '',
      short_name: '',
      academic_degree: '',
      total_credits: 0,
      duration: 0,
      createdBy: '',
      redirect: false,
      stagedError: undefined,
      staged: [],
      viewStaged: [],
      stagedNameFilter: '',
      nameFilter: '',
      createProgrammeFlag: false
    }
    this.handleChange = this.handleChange.bind(this)
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.voteUpStaged = this.voteUpStaged.bind(this)
    this.voteDownStaged = this.voteDownStaged.bind(this)
    this.createStagedProgramme = this.createStagedProgramme.bind(this)
    this.createDefinitiveProgramme = this.createDefinitiveProgramme.bind(this)
    this.filterStagedByName = this.filterStagedByName.bind(this)
    this.filterProgrammesByName = this.filterProgrammesByName.bind(this)
  }

  filterProgrammesByName (ev) {
    const name = ev.target.value.toLowerCase()
    this.setState(prevState => {
      let array = [...prevState.programmes]
      array = array.filter(programme => programme.fullName.toLowerCase().includes(name))
      return ({viewProgrammes: array})
    })
  }

  filterStagedByName (ev) {
    const name = ev.target.value.toLowerCase()
    this.setState(prevState => {
      let array = [...prevState.staged]
      array = array.filter(programme => programme.fullName.toLowerCase().includes(name))
      return ({viewStaged: array})
    })
  }

  handleChange (ev) {
    this.setState({
      [ev.target.name]: ev.target.value
    })
  }

  render () {
    return (
      <div className='container'>
        <div className='left-div'>
          {this.state.error
            ? <p> Error getting all the programmes (Maybe there aren´t any programms) </p>
            : <div>
              <h1>All Programmes in ISEL</h1>
              <p> Filter By Name </p>
              <Input.Search
                name='nameFilter'
                placeholder='Search name'
                onChange={this.filterProgrammesByName}
              />
              <List
                itemLayout='vertical'
                size='large'
                bordered
                dataSource={this.state.viewProgrammes}
                renderItem={item => (
                  <List.Item
                    actions={[
                      <IconText
                        type='like-o'
                        id='like_btn'
                        onClick={() =>
                          this.setState({
                            voteUp: true,
                            progID: item.programmeId
                          })}
                        text={item.votes}
                      />,
                      <IconText
                        type='dislike-o'
                        id='dislike_btn'
                        onClick={() =>
                          this.setState({
                            voteDown: true,
                            progID: item.programmeId
                          })}
                      />
                    ]}
                  >
                    <List.Item.Meta
                      title={<Link to={{ pathname: `/programmes/${item.programmeId}` }}> {item.fullName} ({item.shortName})</Link>}
                      description={`Created by ${item.createdBy}`}
                    />
                  </List.Item>
                )}
              />
            </div>
          }
          {this.props.user.reputation.role === 'ROLE_ADMIN'
            ? <Button icon='plus' id='create_btn' type='primary' onClick={() => this.setState({createProgrammeView: true})}>Create Programme</Button>
            : <Button icon='plus' id='create_btn' type='primary' onClick={() => this.setState({stagedProgrammeView: true})}>Create Programme</Button>
          }
        </div>
        <div className='right-div'>
          {this.state.createProgrammeView &&
            <CreateProgramme action={this.createDefinitiveProgramme} />
          }
          {this.state.stagedProgrammeView &&
            <div id='stagedProgrammes'>
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
                    <p>Created at {timestampParser(item.timestamp)}</p>
                    <IconText
                      type='like-o'
                      id='like_btn'
                      onClick={() =>
                        this.setState({
                          stageID: item.stagedId,
                          voteUpStaged: true
                        })}
                      text={item.votes}
                    />
                    <IconText
                      type='dislike-o'
                      id='dislike_btn'
                      onClick={() =>
                        this.setState({
                          voteDownStaged: true,
                          stageID: item.stagedId
                        })}
                    />
                  </List.Item>
                )}
              />
              <Button type='primary' onClick={() => this.setState({createProgrammeForm: true})}>Still want to create?</Button>
              {this.state.createProgrammeForm && <CreateProgramme action={this.createStagedProgramme} />}
            </div>
          }
        </div>
      </div>
    )
  }

  componentDidMount () {
    const uri = config.API_PATH + '/programmes/'
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(programmes => {
        const stagedUri = config.API_PATH + '/programmes/stage'
        fetcher(stagedUri, header)
          .then(stagedProgrammes => this.setState({
            programmes: programmes.programmeList,
            viewProgrammes: programmes.programmeList,
            staged: stagedProgrammes.programmeStageList,
            viewStaged: stagedProgrammes.programmeStageList
          }))
          .catch(stagedError => {
            message.error('Error fetching staged programmes')
            this.setState({
              programmes: programmes,
              viewProgrammes: programmes,
              stagedError: stagedError
            })
          })
      })
      .catch(error => {
        message.error('Error fetching programmes')
        this.setState({error: error})
      })
  }

  createStagedProgramme (data) {
    const body = {
      programme_full_name: data.full_name,
      programme_short_name: data.short_name,
      programme_academic_degree: data.academic_degree,
      programme_total_credits: data.total_credits,
      programme_duration: data.duration
    }
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(body)
    }
    const url = config.API_PATH + '/programmes/stage'
    fetcher(url, options)
      .then(staged => {
        const newItem = {
          fullName: staged.fullName,
          shortName: staged.shortName,
          academicDegree: staged.academicDegree,
          totalCredits: staged.totalCredits,
          duration: staged.duration,
          votes: staged.votes,
          timestamp: staged.timestamp,
          stagedId: staged.stagedId,
          createdBy: staged.createdBy
        }
        message.success('Successfully created staged programme')
        this.setState(prevState => ({
          staged: [...prevState.staged, newItem],
          viewStaged: [...prevState.staged, newItem],
          createProgrammeFlag: false
        }))
      })
      .catch(error => {
        message.error('Error ocurred while creating the programme')
        this.setState({ progError: error, createProgrammeFlag: false })
      })
  }

  createDefinitiveProgramme (data) {
    const body = {
      programme_full_name: data.full_name,
      programme_short_name: data.short_name,
      programme_academic_degree: data.academic_degree,
      programme_total_credits: data.total_credits,
      programme_duration: data.duration
    }
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(body)
    }
    const url = config.API_PATH + '/programmes'
    fetcher(url, options)
      .then(json => {
        const newItem = {
          fullName: json.fullName,
          shortName: json.shortName,
          academicDegree: json.academicDegree,
          totalCredits: json.totalCredits,
          duration: json.duration,
          votes: json.votes,
          timestamp: json.timestamp,
          programmeId: json.programmeId,
          createdBy: json.createdBy
        }
        message.success('Successfully created programme')
        this.setState(prevState => ({
          programmes: [...prevState.programmes, newItem],
          viewProgrammes: [...prevState.programmes, newItem],
          createProgrammeFlag: false
        }))
      })
      .catch(error => {
        message.error('Error ocurred while creating the programme')
        this.setState({ progError: error, createProgrammeFlag: false })
      })
  }

  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const progID = this.state.progID
    const url = `${config.API_PATH}/programmes/${progID}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(url, body)
      .then(_ => this.setState(prevState => {
        let newArray = [...prevState.programmes]
        const index = newArray.findIndex(programme => programme.programmeId === progID)
        newArray[index].votes = prevState.programmes[index].votes + 1
        message.success('Vote up!!')
        return ({
          programmes: newArray,
          voteUp: false
        })
      }))
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot vote up')
        }
        this.setState({voteUp: false})
      })
  }

  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const progID = this.state.progID
    const url = `${config.API_PATH}/programmes/${progID}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(url, body)
      .then(_ => this.setState(prevState => {
        let newArray = [...prevState.programmes]
        const index = newArray.findIndex(programme => programme.programmeId === progID)
        newArray[index].votes = prevState.programmes[index].votes - 1
        message.success('Vote Down!!')
        return ({
          programmes: newArray,
          voteDown: false
        })
      }))
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot vote down')
        }
        this.setState({voteDown: false})
      })
  }

  voteUpStaged () {
    const voteInput = {
      vote: 'Up'
    }
    const stageID = this.state.stageID
    const url = `${config.API_PATH}/programmes/stage/${stageID}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(url, body)
      .then(_ => this.setState(prevState => {
        let newArray = [...prevState.staged]
        const index = newArray.findIndex(programme => programme.stagedId === stageID)
        newArray[index].votes = prevState.staged[index].votes + 1
        message.success('Successfully voted up')
        return ({
          staged: newArray,
          voteUpStaged: false
        })
      }))
      .catch(_ => {
        message.error('Error while processing your vote')
        this.setState({voteUpStaged: false})
      })
  }

  voteDownStaged () {
    const voteInput = {
      vote: 'Down'
    }
    const stageID = this.state.stageID
    const url = `${config.API_PATH}/programmes/stage/${stageID}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(url, body)
      .then(_ => this.setState(prevState => {
        let newArray = [...prevState.staged]
        const index = newArray.findIndex(programme => programme.stagedId === stageID)
        newArray[index].votes = prevState.staged[index].votes - 1
        message.success('Successfully voted down')
        return ({
          staged: newArray,
          voteDownStaged: false
        })
      }))
      .catch(_ => {
        message.error('Error while processing your vote')
        this.setState({voteDownStaged: false})
      })
  }

  componentDidUpdate () {
    if (this.state.voteDown) {
      this.voteDown()
    } else if (this.state.voteUp) {
      this.voteUp()
    } else if (this.state.voteDownStaged) {
      this.voteDownStaged()
    } else if (this.state.voteUpStaged) {
      this.voteUpStaged()
    }
  }
}

export default (props) => (
  <Layout>
    <Programmes />
  </Layout>
)
