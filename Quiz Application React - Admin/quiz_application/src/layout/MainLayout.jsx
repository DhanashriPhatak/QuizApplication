import React, { useEffect } from 'react';
import Header from '../components/common/Header';
import SideNav from '../components/common/SideNav';
import Footer from '../components/common/Footer';
import { Outlet } from 'react-router-dom';
import $ from 'jquery';

const MainLayout = () => {
  useEffect(() => {
    document.body.classList.add('sidebar-mini', 'layout-fixed', 'layout-navbar-fixed', 'sidebar-collapse');
    if ($.fn && $.fn.PushMenu) {
      new $.PushMenu($('[data-widget="pushmenu"]'));
    }
  }, []);
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