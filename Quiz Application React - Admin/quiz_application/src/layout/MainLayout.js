import React from 'react';
import Header from '../components/common/Header';
import SideNav from '../components/common/SideNav';
import Footer from '../components/common/Footer';
import { Outlet } from 'react-router-dom';

const MainLayout = () => {
  return (
    <div className='app-wrapper'>
        <Header></Header>
        <SideNav></SideNav>
        <Outlet></Outlet>
        <Footer></Footer>
    </div>
  )
}

export default MainLayout