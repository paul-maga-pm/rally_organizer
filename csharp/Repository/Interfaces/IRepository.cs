using Domain;
using Domain.Models;
using Exceptions;
using System.Runtime.Serialization;

namespace Repository
{
    public interface IRepository<ID, M> where M : Identifiable<ID>
    {
        M Add(M model);
        IEnumerable<M> FindAll();
        M FindOne(M model);

    }
}