import React from 'react'
import { Menu } from 'antd'

export default (props) => {
  return (
    <Menu
      id='navbar'
      theme='light'
      mode='horizontal'
      style={{ lineHeight: '64px' }}
    >
      <Menu.Item key='home'>
        <img src='logo.png' width='10%' />
      </Menu.Item>
    </Menu>
  )
}
