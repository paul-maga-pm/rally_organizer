using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


using Repository.Interfaces;
using Domain.Models;

namespace Business.Services
{
    public class ParticipantService
    {
        private IParticipantRepository participantRepository;

        public ParticipantService(IParticipantRepository participantRepository)
        {
            this.participantRepository = participantRepository;
        }

        public Participant AddParticipant(Team participantsTeam, Rally rallyParticipatesTo, String participantName)
        {
            Participant participant = new Participant(rallyParticipatesTo, participantsTeam, participantName);
            return participantRepository.Add(participant);
        }

        public ICollection<Participant> GetAllMembersOfTeam(String teamName)
        {
            return participantRepository.GetMembersOfTeam(teamName);
        }

        public Participant? GetParticipantByName(String participantName)
        {
            return participantRepository.GetByName(participantName);
        }
    }
}
