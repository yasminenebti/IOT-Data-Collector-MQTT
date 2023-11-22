import axios from "axios";
import { useState } from "react";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { _baseURL } from "../utils/_baseURL";


const PublishEvent = () => {
  const [data, setData] = useState({ deviceId: 0, payload: "" });

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      await axios.post(`${_baseURL}/api/publish`, data);
      console.log(data);
      setData({ deviceId: 0, payload: "" })
      toast(' Message published successfully!', {
        position: "top-left",
        autoClose: 3000,
        theme: "dark",
        });
    } catch (error) {
      console.log(error.message);
      toast.error('Failed to publish message', {
        position: "top-left",
        autoClose: 3000,
      });
    }
  };
  const handleChange = (e) => {
    const { name, value } = e.target;
    setData((prevData) => ({ ...prevData, [name]: value }));
  };

  return (
    <div>
     
      <h2 className=" font-bold text-2xl">Start Publishing for the temperature topic...</h2>
      <form onSubmit={handleSubmit} className="space-y-6 mt-20">
        <div>
          <p className="text-md font-medium mb-2">
            Device
          </p>
          <input
            placeholder="Enter your device Id"
            type="number"
            name="deviceId"
            onChange={handleChange}
            value={data.deviceId}
            className="py-2 px-2 w-full rounded-md border-2"
          />
        </div>
        <div>
          <p className="text-md font-medium mb-2">
            Payload
          </p>
          <input
            placeholder="Enter your payload"
            type="text"
            name="payload"
            onChange={handleChange}
            value={data.payload}
            className="py-2 px-2 w-full rounded-md border-2"
          />
        </div>
        <div>
          <button type="submit" className="w-full">
            Publish
          </button>
        </div>
      </form>

      <ToastContainer />

  

    </div>
  );
};

export default PublishEvent;