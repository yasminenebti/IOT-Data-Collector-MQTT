import { useNavigate } from "react-router";
import "../App.css";
import { PieChart, Pie, Cell, Legend, Tooltip } from 'recharts';
import { useEffect, useState } from "react";
import axios from "axios";
import { _baseURL } from "../utils/_baseURL";

const Home = () => {
  const navigate = useNavigate();
  const [lightCount , setLightCount] = useState(0);
  const [tempCount , setTempCount] = useState(0);
  const data = [
    { name: 'Light Topic', value: lightCount },
    { name: 'Temperature Topic', value: tempCount }
   
  ];

  const colors = ['#8884d8', '#b7e4c7'];


  const handleMessageNavigate = () => {
    navigate("/messages");
  };

  const handlePublishNavigate = () => {
    navigate("/publishEvent");
  };

  const getLightCount = async () => {
    
    try {
      const res = await axios.get(`${_baseURL}/api/lightTopic`);
      setLightCount(res.data);

    } catch (error) {
      console.log(error.message);
    }
  };

  const getTempCount = async () => {
    
    try {
      const res = await axios.get(`${_baseURL}/api/tempTopic`);
      setTempCount(res.data);
    
    } catch (error) {
      console.log(error.message);
    }
  };

  useEffect(() => {
    getLightCount();
    getTempCount();

  },[])
  return (
    <>
      <div></div>
      <div className="border">
			<strong className="text-gray-700 font-medium">Database Update</strong>
			<div className="mt-3 w-full flex-1 text-xs">
      <PieChart width={400} height={400}>
      <Pie
        data={data}
        dataKey="value"
        nameKey="name"
        cx="50%"
        cy="50%"
        outerRadius={80}
        fill="#8884d8"
        label
      >
        {data.map((_, index) => (
								<Cell key={`cell-${index}`} fill={colors[index % colors.length]} />
							))}
        
        
      </Pie>
      <Tooltip />
      <Legend />
    </PieChart>
			</div>
		</div>
      <div className="flex justify-center gap-2 mt-4">
        <button onClick={handlePublishNavigate}>publish new event</button>
        <button onClick={handleMessageNavigate}> get Mqtt Updates</button>
      </div>
    </>
  );
};

export default Home;