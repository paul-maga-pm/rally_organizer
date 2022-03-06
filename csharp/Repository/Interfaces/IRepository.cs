using Domain;
using Domain.Models;

namespace Repository
{
    public interface IRepository<ID, E> where E : Identifiable<ID>
    {
        E Add(E model);
        E Remove(ID id);
        E FindById(ID id);
        
        ICollection<E> GetAll();

        bool Update(E model);
    }
}