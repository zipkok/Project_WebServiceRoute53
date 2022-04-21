export const state = () => ({
  recordsItems: [],
})

export const mutations = {
  loadRecordSetItems(state, payload) {
    state.recordsItems = payload
  },
}

export const actions = {
  //  Load a ListRecordSets using Account
  async loadRecordSetItems({ commit, state }, payload) {
    await this.$axios
      .get(process.env.backendUrl + '/recordsets/' + payload.HostedZoneId)
      .then((res) => {
        commit('loadRecordSetItems', res.data)
      })
      .catch(() => {})
  },

  async createRecordSets({ commit, state }, payload) {
    await this.$axios
      .post(process.env.backendUrl + '/recordsets/' + payload.hostedZoneId)
      .then((result) => {})
      .catch(() => {})
  },

  async deleteRecordSets({ commit, state }, payload) {
    await this.$axios
      .delete(process.env.backendUrl + '/recordsets/' + payload.HostedZoneId)
      .then((result) => {})
      .catch(() => {})
  },

  async updateRecordSetItems({ commit, state }, payload) {
    console.log('payload' + JSON.stringify(payload))
    await this.$axios
      .put(process.env.backendUrl + '/recordsets/' + payload.HostedZoneId)
      .then((result) => {})
      .catch(() => {})
  },
}
