import axios from "axios";
import { BiRefresh } from "react-icons/bi";
import { _baseURL } from "../utils/_baseURL";
import { useEffect, useState } from "react";

const LatestMessages = () => {
  const [data, setData] = useState({ nbMessages: 0, date: "" });

  const getUpdates = async () => {
    try {
      const res = await axios.get(`${_baseURL}/api/latestMessage`);
      setData(res.data);
    } catch (error) {
      console.log(error.message);
    }
  };

  useEffect(() => {
    getUpdates();
  }, []);
  return (
    <div>
     
    

      <div className=" flex items-center gap-2 px-1 py-3 mb-16">
				<BiRefresh onClick={getUpdates}  color="#9a8c98" fontSize={60}  className="hover:cursor-pointer" />
				<h2 className=" text-2xl">New Messages updates from Mqtt broker</h2>
			</div>

      <div className="flex justify-center gap-12 mt-4">
        <div className="text-3xl">messages : {data.nbMessages}</div>
        <div className="text-3xl">{data.date}</div>
      </div>
    </div>
  );
};

export default LatestMessages;
