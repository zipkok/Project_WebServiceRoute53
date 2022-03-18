<template>
  <div>
    <v-container>
      <v-card>
        <v-card-title> 어쩔 DNS </v-card-title>
        <!-- <div>Debug : {{ values }}</div> -->
        <v-container>
          <v-card-actions>
            <!-- TODO: autocomplete multiple 설정 추가 -->
            <v-autocomplete
              v-model="values"
              :items="accountItems"
              outlined
              dense
              chips
              small-chips
              label="도메인을 선택하고 오른쪽 Reload 눌러."
              class="pr-3 pt-7"
            />

            <!-- Method: addHostZoneId -->
            <account-dialog />

            <!-- Method: loadAccountItems -->
            <div class="pl-3">
              <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    color="red"
                    v-bind="attrs"
                    v-on="on"
                    @click="loadAccountItems"
                  >
                    <v-icon>mdi-reload</v-icon>
                  </v-btn>
                </template>
                <span>DNS Zone 새로고침</span>
              </v-tooltip>
            </div>
          </v-card-actions>
        </v-container>
      </v-card>
    </v-container>

    <v-container>
      <v-card>
        <v-card-title>
          <!-- DataTable Header 윗 부분, 검색 -->
          <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="어떤 도메인 찾아?"
            single-line
            hide-details
            outlined
            class="pr-5"
            height="20"
          />

          <!-- Methods: reloadHostZoneIds -->
          <!-- DataTable Header 윗 부분, 레코드 Reload -->
          <v-card-actions>
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                  v-bind="attrs"
                  v-on="on"
                  @click="reloadHostZoneIds(values)"
                >
                  <v-icon> mdi-cursor-default-click-outline </v-icon>
                </v-btn>
              </template>
              <span>Record 갱신</span>
            </v-tooltip>
          </v-card-actions>
        </v-card-title>

        <v-data-table :headers="headers" :items="recordsItems" :search="search">
          <template v-slot:body="{ items }">
            <tbody>
              <tr v-for="item in items" :key="item.index">
                <td>{{ item.recordName }}</td>
                <td>{{ item.type }}</td>
                <td>
                  {{ item.recordSetsItems }}
                </td>
                <td>{{ item.expire }}</td>
                <td>{{ item.routingPolicy }}</td>
              </tr>
            </tbody>
          </template>
        </v-data-table>
      </v-card>
    </v-container>

    <v-container>
      <v-card>
        {{ recordsItems.recordSetsItems }}
      </v-card>
    </v-container>
    <snackbar :snackbar="snackbar" />
  </div>
</template>

<script>
import AccountDialog from '~/components/account/dialog.vue'
import Snackbar from '~/components/common/snackbar.vue'

export default {
  components: {
    AccountDialog,
    Snackbar,
  },

  data() {
    return {
      // autoCompletes
      values: '',

      // Error Message
      snackbar: { bool: false, content: '' },

      // Table
      search: '',
      headers: [
        { text: 'Record Name', value: 'recordName' },
        { text: 'Type', value: 'type' },
        { text: 'Value', value: 'recordSetsItems' },
        { text: 'TTL', value: 'expire' },
        { text: 'Routing Policy', value: 'routingPolicy' },
      ],
    }
  },

  computed: {
    recordsItems() {
      return this.$store.state.record.recordsItems
    },

    accountItems() {
      return this.$store.state.account.accountItems
      // return this.$store.state.record.test
    },
  },

  mounted() {
    this.$store.dispatch('account/loadAccountItems')
  },

  methods: {
    addHostZoneId() {
      // Account 등록하면, reload 해야함.
    },

    loadAccountItems() {
      this.$store.dispatch('account/loadAccountItems')
    },

    async reloadHostZoneIds(HostedZoneId) {
      if (this.values === '') {
        this.snackbar.bool = true
        this.snackbar.content = '계정을 선택해주세요.'
      } else {
        const req = HostedZoneId.substring(0, 20)
        await this.$store.dispatch('record/loadRecordSetItems', {
          HostedZoneId: req,
        })
      }
      /*
      TODO: Account 정보를 받아서 /recordsets/{HostedZoneId} POST
            만약 다른 값이 있으면 저장, 모두 같으면 저장 안함.
            Account 정보를 받아서 /recordsets/{HostedZoneId} GET
            그 값을 Table에 출력
      */
      // FIXME: {HostedZoneId}로 변경해야함.
    },
  },
}
</script>

<style scoped></style>
