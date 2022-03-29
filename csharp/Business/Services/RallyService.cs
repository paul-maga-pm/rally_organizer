using Domain.Models;
using Repository.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Business.Services
{
    public class RallyService
    {
        private IRallyRepository rallyRepository;

        public RallyService(IRallyRepository rallyRepository)
        {
            this.rallyRepository = rallyRepository;
        }

        public Rally AddRally(int engineCapacity)
        {
            Rally rally = new Rally(engineCapacity);
            return rallyRepository.Add(rally);
        }

        public ICollection<Rally> GetAllRallies()
        {
            return rallyRepository.FindAll();
        }
    }
}
