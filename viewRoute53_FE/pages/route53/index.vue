<template>
  <div>
    <v-container>
      <v-card elevation="20">
        <v-card-actions>
          <v-container>
            <!-- TODO: autocomplete multiple 설정 추가 -->
            <v-autocomplete
              v-model="values"
              :items="accountList"
              outlined
              clearable
              small-chips
              dense
              label="Domain Zone을 선택하세요."
              class="pl-1 pr-2"
              @click:clear="clearAutoComplete()"
            />
          </v-container>

          <!-- Methods: reloadHostZoneIds -->
          <!-- DataTable Header 윗 부분, 레코드 Reload -->
          <div class="pb-6 pr-5">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                  color="primary"
                  v-bind="attrs"
                  v-on="on"
                  @click="loadAccountItems(values)"
                >
                  <v-icon> mdi-reload </v-icon>
                </v-btn>
              </template>
              <span>계정 재갱신</span>
            </v-tooltip>
          </div>
        </v-card-actions>
      </v-card>
    </v-container>

    <v-container>
      <v-card elevation="20">
        <v-card-title>
          <!-- DataTable Header 윗 부분, 검색 -->
          <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="*Routing Value로는 검색 불가"
            class="pr-5"
          />
          <div class="pr-3">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                  color="primary"
                  v-bind="attrs"
                  v-on="on"
                  @click="reloadHostZoneIds(values)"
                >
                  <v-icon> mdi-cursor-default-click-outline </v-icon>
                </v-btn>
              </template>
              <span>레코드 재갱신</span>
            </v-tooltip>
          </div>
        </v-card-title>

        <v-data-table :headers="headers" :items="recordsItems" :search="search">
          <template v-slot:body="{ items }">
            <tbody>
              <tr v-for="item in items" :key="item.index">
                <td>{{ item.recordName }}</td>
                <td>{{ item.type }}</td>
                <td>{{ item.expire }}</td>
                <td>{{ item.routingPolicy }}</td>
                <td>
                  {{
                    filterRouting(
                      item.routeGeoLocation,
                      item.routeLatencyRegion,
                      item.routeWeight
                    )
                  }}
                </td>
                <td v-if="filterValue(item.type)">
                  <a
                    :href="'http://localhost.com/' + item.recordSetsValue"
                    target="_blank"
                  >
                    {{ item.recordSetsValue }}
                  </a>
                </td>
                <td v-else>{{ item.recordSetsValue }}</td>
              </tr>
            </tbody>
          </template>
        </v-data-table>
      </v-card>
    </v-container>

    <snackbar :snackbar="snackbar" />
  </div>
</template>

<script>
import Snackbar from '~/components/common/snackbar.vue'

export default {
  components: {
    Snackbar,
  },

  async fetch() {
    await this.$store.dispatch('account/loadAccountItems')
    await this.filterLoadAccountItems(this.accountItems)

    this.snackbar.bool = true
    this.snackbar.content = '계정 정보 로드 성공'
  },

  data() {
    return {
      // autoCompletes
      values: '',
      accountList: [],
      account: '',

      // Error Message
      snackbar: { bool: false, content: '' },

      // Table
      search: '',
      headers: [
        {
          text: 'Record Name',
          width: '15%',
          value: 'recordName',
          class: 'warning--text',
        },
        {
          text: 'Type',
          width: '8%',
          value: 'type',
          class: 'warning--text ',
        },
        {
          text: 'TTL',
          width: '10%',
          value: 'expire',
          class: 'warning--text ',
        },
        {
          text: 'Routing Policy',
          width: '17%',
          value: 'routingPolicy',
          class: 'warning--text',
        },
        {
          text: 'Routing Value',
          width: '15%',
          value: 'filterRouting',
          class: 'warning--text ',
        },
        {
          text: 'Record Value',
          width: '20%',
          value: 'recordSetsValue',
          class: 'warning--text ',
        },
      ],
    }
  },

  computed: {
    recordsItems() {
      return this.$store.state.record.recordsItems
    },

    accountItems() {
      return this.$store.state.account.accountItems
    },
  },

  methods: {
    clearAutoComplete() {
      this.values = ''
      this.account = ''
    },

    async loadAccountItems() {
      this.clearAutoComplete()
      await this.$store
        .dispatch('account/loadAccountItems')
        .then((res) => {
          this.snackbar.bool = true
          this.snackbar.content = '계정 새로고침 성공 ' + res
          this.snackbar.color = 'success'
          this.filterLoadAccountItems(this.accountItems)
        })
        .catch((err) => {
          this.snackbar.bool = true
          this.snackbar.content = '계정 새로고침 실패: ' + err
          this.snackbar.color = 'red'
        })
    },

    async reloadHostZoneIds() {
      if (this.values === '' || this.values === null) {
        this.snackbar.bool = true
        this.snackbar.content = '계정을 선택해주세요.'
        this.snackbar.color = 'red'
      } else {
        const req = this.values.split('/')
        const trimReq = req[0].trim()
        await this.$store.dispatch('record/updateRecordSetItems', {
          HostedZoneId: trimReq,
        })
        await this.$store.dispatch('record/loadRecordSetItems', {
          HostedZoneId: trimReq,
        })

        this.snackbar.bool = true
        this.snackbar.content = '레코드 로드 성공.'
        this.snackbar.color = 'success'
      }
    },

    async filterLoadAccountItems(AccountItems) {
      try {
        this.accountList = []
        for (const idx in AccountItems) {
          const getterAccount =
            AccountItems[idx].hostedZoneId +
            ' / ' +
            AccountItems[idx].team +
            ' / ' +
            AccountItems[idx].accountName +
            ' / ' +
            AccountItems[idx].hostedZoneName
          await this.accountList.push(getterAccount)
          this.snackbar.bool = true
          this.snackbar.content = '계정 정보 재갱신'
          this.snackbar.color = 'success'
        }
      } catch (err) {
        this.snackbar.bool = true
        this.snackbar.content = '계정 정보 로드 실패: ' + err
        this.snackbar.color = 'red'
      }
    },

    filterRouting(geoValue, latencyValue, weightValue) {
      if (geoValue !== '-') return geoValue
      else if (latencyValue !== '-') return latencyValue
      else if (weightValue !== '-') return weightValue
      else return '-'
    },

    filterValue(getType, getValue) {
      if (getType === 'A') return true
      else return false
    },
  },
}
</script>

<style scoped>
a {
  text-decoration: none;
  color: blue;
}

.v-data-table-header {
  background-color: black;
}

.custom-header {
  color: blue;
  background-color: blue;
}
</style>
