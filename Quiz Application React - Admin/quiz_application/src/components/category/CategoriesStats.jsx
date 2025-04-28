import React, { useState,useEffect } from 'react'
import Spinner from '../common/Spinner';
import CategoryStatsCard from './CategoryStatsCard';
import { getCategoryStats } from '../../services/QuestionService';

const CategoriesStats = ({refreshCategories}) => {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [categoryStats,setCategoryStats] = useState([]);

  useEffect(()=>{
      setLoading(true);
      getCategoryStats()
      .then((res)=>{
        setCategoryStats(res.data);
        setLoading(false);
      })
      .catch(error=>{
        setError("Failed to fetch categories");
        setLoading(false);
      })
    }, [refreshCategories]);

    

  return (
    <>
      <div className="app-content"> {/*begin::Container*/}
        <div className="container-fluid">
          <div className="row g-4 mb-4">
          {loading?<Spinner/>
          :error?(<p className="text-danger">{error}</p>)
          :(categoryStats.map((category,index)=>{
            return (
              <CategoryStatsCard
                key={index}
                data={category}
              ></CategoryStatsCard>
            )
          })
          )
          }
          </div>
        </div>
      </div>
    </>
  )
}

export default CategoriesStats