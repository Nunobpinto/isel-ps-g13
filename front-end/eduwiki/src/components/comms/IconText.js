import React from 'react'
import { Button } from 'antd'

export default ({type, text, onClick, id}) => (
  <span>
    <Button id={id} icon={type} shape='circle-outline' onClick={onClick} style={{ marginRight: 8 }} />
    {text}
  </span>
)
